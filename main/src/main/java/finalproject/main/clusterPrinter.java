package finalproject.main;

public class clusterPrinter {
	public void sortArray(String[] data,int[] A,int size)
	{
		int temp=0;
		int j=0;
		String buff="";
		for(int i=0;i<size;i++)
		{		j=i;
			while (j>0 && A[j]<A[j-1] )
			{
				buff=data[j-1];
				data[j-1]=data[j];
				data[j]=buff;
				
				temp=A[j-1];
				A[j-1]=A[j];
				A[j]=temp;
				
				j--;
			}
		}
	}
	public void printConsole(int[] A,String[] data,int size)
	{
		sortArray(data,A,size);
		int tempClus=0;
        int i=0;
        while(i<size)
		{
        	tempClus=A[i];
		    System.out.println("Cluster: "+tempClus);
			while(tempClus==A[i])
			{
				System.out.println(" "+data[i]);
				i++;
				if(i==size)
					break;
			}
		}
    }	
}
