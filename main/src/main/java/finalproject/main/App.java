package finalproject.main;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.mahout.clustering.canopy.CanopyDriver;
import org.apache.mahout.clustering.classify.WeightedPropertyVectorWritable;
import org.apache.mahout.clustering.kmeans.KMeansDriver;
import org.apache.mahout.math.NamedVector;
import org.apache.mahout.text.SequenceFilesFromDirectory;
import org.apache.mahout.vectorizer.SparseVectorsFromSequenceFiles;

import com.google.common.base.Charsets;

import java.io.*;

public class App {

	//Working Perfectly for the following parameters for clustering drinks and countries data
	
	//PARAMETERS : KMEANS	compilation
    static String numItterations ="6000";
    static String convergenceDelta="0.01";
    static String numClusters="4";
    //static String disMeasure="org.apache.mahout.common.distance.CosineDistanceMeasure";
    static String disMeasure="org.apache.mahout.common.distance.TanimotoDistanceMeasure";
    
    static String mdisMeasure="org.apache.mahout.common.distance.CosineDistanceMeasure";
    static String t1="0.72";
    static String t2="0.2";

    //PARAMETERS FOR CONSOLE OUTPUT
    static int docs=200;
    
    //DIRECTORIES
    static String parent_dir="main/textClassification/";
	static String rawInputDir ="main/rawInput/";
	
	
	public static void main(String[] args) throws Exception{
		String rawToSeqDir = parent_dir+"/rawToSequence";
		String seqToVecDir = parent_dir+"/seqToVectors";
		String kmeansResultsDir =parent_dir+"/kmeansResults";
		String canopyResultsDir =parent_dir+"/canopyResults";
		String canopyOut = parent_dir+"/canopyResults/clusters-0-final";
		
		String cmd = "python /get_rss.py";        
		 
		System.out.println("I am writing here");
		// create runtime to execute external command
		Runtime rt = Runtime.getRuntime();
		Process pr = rt.exec(cmd);
		
		System.out.println("finished crawling");
		//Since main is static method of class SeqFileFromDir therefore we need to call it like class.property and dont need to create the object
		SequenceFilesFromDirectory.main(new String[] {"--input",rawInputDir.toString(), "--output", rawToSeqDir.toString(), "--chunkSize","64", "--charset",Charsets.UTF_8.name(),"--overwrite"});
		
		SparseVectorsFromSequenceFiles.main(new String[] {"--input",rawToSeqDir.toString(),"--output",seqToVecDir.toString(),"--maxNGramSize","3","--namedVector", "--overwrite"});

		String tfidfVecDir = seqToVecDir+"/tfidf-vectors";

		  
		Path outPath = new Path(canopyResultsDir);
		Path inPath = new Path(tfidfVecDir);
		CanopyDriver.main(new String[] {"--input",tfidfVecDir, "--output", canopyResultsDir, "--distanceMeasure",mdisMeasure,"--t1",t1,"--t2",t2, "--overwrite"});

		KMeansDriver.main(new String[] {"--input",tfidfVecDir, "--output", kmeansResultsDir, "--convergenceDelta",convergenceDelta,"--maxIter",numItterations,"--distanceMeasure",disMeasure,"--clusters",canopyOut,"--clustering","--overwrite"});
	
	    
		
		String[] vecNames=new String[docs];
		int[] cluster=new int[docs];
		
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(URI.create(rawToSeqDir),conf);
    	SequenceFile.Reader reader = new SequenceFile.Reader(fs, new Path(kmeansResultsDir+"/clusteredPoints/part-m-00000"), conf);
		IntWritable key = new IntWritable();
		WeightedPropertyVectorWritable value = new WeightedPropertyVectorWritable();
		
	//   PrintWriter writer = new PrintWriter("ForUpload.txt", "UTF-8");
	   
		clusterPrinter abc=new clusterPrinter();
		int k = 0; //NUMBER OF INPUTS OR DOCUMENTS
		ArrayList<String> vertorNameList=new ArrayList<String>();
		ArrayList<Integer> clusterList=new ArrayList<Integer>();
		while (reader.next(key, value)) {
			NamedVector vector = (NamedVector) value.getVector();
			String vectorName = vector.getName();
		    vecNames[k]=vectorName;
		    cluster[k]=(Integer.parseInt(key.toString()));
	//	    writer.println(vectorName + " , "+key.toString()); 
		    String tmp =vectorName.substring(1,vectorName.indexOf("."));
			System.out.println(tmp + ","+key.toString());
			vertorNameList.add(new String(tmp));
			clusterList.add(cluster[k]);
			
			k++;
		}
		abc.printConsole(cluster,vecNames,12);
		
		
	MySqlConnect db = new MySqlConnect();
	db.connect_db(vertorNameList,clusterList);
	
//		System.out.println("NUMBER OF FILES: "+k);
		reader.close();
//		 writer.close();

	
		//Updating articles into Mysql database
		InputStream in = new App().getClass().getResourceAsStream("/BBC_Index_FileName_Matching.txt");
		BufferedReader buffer = new BufferedReader(new InputStreamReader(in));
		String line = buffer.readLine();
		
		InputStream in1 = new App().getClass().getResourceAsStream("/BBC_Link_Matching.txt");
		BufferedReader buffer2 = new BufferedReader(new InputStreamReader(in1));
		String line2 = buffer2.readLine();
		
		
		db.delete_Article();
		while (line!=null){
			StringTokenizer str = new StringTokenizer(line);
			StringTokenizer str1 = new StringTokenizer(line2);
			
			String filename = str.nextToken();
			str1.nextToken();
			
			
			System.out.println(filename);
			String article="";
			String link="";
			InputStream sp = new App().getClass().getResourceAsStream("/"+filename+".txt");

			while(str.hasMoreTokens()){
				article += str.nextToken()+" ";
			}
			
			while(str1.hasMoreTokens()){
				link += str1.nextToken()+" ";
			}
			
			BufferedReader buffer1 = new BufferedReader(new InputStreamReader(sp));
			String line1 = buffer1.readLine();
			String block="";
				
				while(line1!=null){
					block+=line1+" ";
					line1 = buffer1.readLine();
				}
				System.out.println("Updating BBC World");
//				System.out.println(filename+" "+article+" "+block);
				System.out.println(link);
				
				db.insert_article(filename, article, block,link);
				buffer1.close();
				
				line = buffer.readLine();
				line2 = buffer2.readLine();
			}
			
		buffer.close();
		buffer2.close();
		
		//Updating articles into Mysql database
				
				in = new App().getClass().getResourceAsStream("/NYT_Index_FileName_Matching.txt");
				buffer = new BufferedReader(new InputStreamReader(in));
				line = buffer.readLine();
				
				in1 = new App().getClass().getResourceAsStream("/NYT_Link_Matching.txt");
				buffer2 = new BufferedReader(new InputStreamReader(in1));
				line2 = buffer2.readLine();
				
				while (line!=null){
					StringTokenizer str = new StringTokenizer(line);
					String filename = str.nextToken();
					
					StringTokenizer str1 = new StringTokenizer(line2);
					str1.nextToken();
					
					System.out.println(filename);
					String article=" ";
					String link="";
					
					InputStream sp =new App().getClass().getResourceAsStream("/"+filename+".txt");
					System.out.println(sp);
					BufferedReader buffer1 = new BufferedReader(new InputStreamReader(sp));
						while(str.hasMoreTokens()){
							article += str.nextToken()+ " ";
						}
						String line1 = buffer1.readLine();
						String block=" ";
						while(line1!=null){
							block+=line1+" ";
							line1 = buffer1.readLine();
						}
						
						while(str1.hasMoreTokens()){
							link += str1.nextToken()+" ";
						}
							
						
						db.insert_article(filename, article, block,link);
						buffer1.close();
						line = buffer.readLine();
						line2 = buffer2.readLine();
					}
					
				buffer.close();
				buffer2.close();
		
		
	}
}


