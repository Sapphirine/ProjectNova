package finalproject.main;

import java.sql.*;
import java.util.ArrayList;


public class MySqlConnect {

	   // JDBC driver name and database URL
	   static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	   static final String DB_URL = "jdbc:mysql://projectnovatry.ca7v4yqqbrky.us-west-2.rds.amazonaws.com:3306/ProjectNovaTry";

	   //  Database credentials
	   static final String USER = "BigData";
	   static final String PASS = "bigdataanalysis";
	   
	   public void connect_db(ArrayList<String> v, ArrayList<Integer> c) {
			
	   Connection conn = null;
	   Statement stmt = null;
	  
	   
	   try{	      
	      //STEP 2: Register JDBC driver
	      Class.forName("com.mysql.jdbc.Driver");

	      //STEP 3: Open a connection
	      System.out.println("ConnectTrace();ing to database...");
	      conn = DriverManager.getConnection(DB_URL,USER,PASS);

	      //STEP 4: Execute a query

	      System.out.println("Inserting records into table Cluster ...");
	      stmt = conn.createStatement();
	      
	      System.out.println("Deleted Clusters");
	     String sql = "delete from Cluster;";
	     stmt.executeUpdate(sql);
	     
	     System.out.println("Inserting into cluster values");
	      int size= (v.size()>c.size()?c.size():v.size());
	    for(int i=0;i<size;i++){
	    	String indexname =v.get(i);
	    	int clusternum=c.get(i);
	        sql = "insert into Cluster values('"+indexname+"',"+clusternum+");";
		      stmt.executeUpdate(sql);
		      System.out.println("Done!");
	    }  
    
	      
	   }catch(SQLException se){
	      //Handle errors for JDBC
	      se.printStackTrace();
	   }catch(Exception e){
	      //Handle errors for Class.forName
		   e.printStackTrace();
	   }finally{
	      //finally block used to close resources
	      try{
	         if(stmt!=null)	      
	            conn.close();
		    	
	      }catch(SQLException se){
	      }// do nothing }
	      try{
	         if(conn!=null)
	            conn.close();
	      }catch(SQLException se){
	         se.printStackTrace();
	      }//end finally try
	   }//end try
	   System.out.println("Goodbye!");
	}//end main
	
	   
	   public void delete_Article() throws ClassNotFoundException, SQLException{
		   Connection conn = null;
		   Statement stmt = null;
		   String  sql = "delete from Article;";
		   System.out.println("Deleting the Article database...");
		   Class.forName("com.mysql.jdbc.Driver");
		   conn = DriverManager.getConnection(DB_URL,USER,PASS);
		      stmt = conn.createStatement();
		      stmt.executeUpdate(sql);
	   }
	   
	   
	   public void insert_article(String filename, String article,String block,String link){
		   Connection conn = null;
		   Statement stmt = null;
		  
		   
		   try{	      
		      //STEP 2: Register JDBC driver
		      Class.forName("com.mysql.jdbc.Driver");

		      //STEP 3: Open a connection
		      System.out.println("ConnectTrace();ing to database...");
		      conn = DriverManager.getConnection(DB_URL,USER,PASS);

		      //STEP 4: Execute a query
		      
		     System.out.println("Inserting records into Article Database ...");
		      stmt = conn.createStatement();
		    
//		      int size = filename.size();
		      String sql=null;
		     
//		      for(int i=0;i<size;i++){
		    	String indexname =filename;
//		    	System.out.println(indexname);
		    	String title1=article;
//		    	System.out.println(title1);
		    	String description1 = block;
//		    	System.out.println(description1);
		        sql = "insert into Article values('"+indexname+"','"+title1+"','"+ description1 +"','"+link+"');";
			      stmt.executeUpdate(sql);
//		    }    
		      
		   }catch(SQLException se){
		      //Handle errors for JDBC
		      se.printStackTrace();
		   }catch(Exception e){
		      //Handle errors for Class.forName
			   e.printStackTrace();
		   }finally{
		      //finally block used to close resources
		      try{
		         if(stmt!=null)	      
		            conn.close();
		      }catch(SQLException se){
		      }// do nothing }
		      try{
		         if(conn!=null)
		            conn.close();
		      }catch(SQLException se){
		         se.printStackTrace();
		      }//end finally try
		   }//end try
		   System.out.println("Goodbye!");
		}//end main
	   }

