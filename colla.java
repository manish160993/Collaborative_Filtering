import java.util.*;
import java.io.*;
import java.util.concurrent.TimeUnit;
public class colla{
	
	
	public static void main(String[] args){
		Scanner scan=new Scanner(System.in);
		HashMap<Integer,HashMap<Integer,Double>> ratings_movie = new HashMap<Integer,HashMap<Integer,Double>>();
		HashMap<Integer,HashMap<Integer,Double>> ratings_user = new HashMap<Integer,HashMap<Integer,Double>>();
		HashMap<Integer,Double> avg_rating = new HashMap<Integer,Double>();
		System.out.println("Enter training link");
		String fileName = scan.nextLine();
		System.out.println("Enter test link");
		String fileName1 = scan.nextLine();
		System.out.println("Number of Test Data you want to check");
		int check=scan.nextInt();
        // This will reference one line at a time
        String line = null;

        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader = 
                new FileReader(fileName.trim());

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = 
                new BufferedReader(fileReader);

			System.out.println("It starts");
			int i=0;
            while((line = bufferedReader.readLine()) != null) {
                String[] ar=line.split(",");
				if(!ratings_movie.containsKey(Integer.parseInt(ar[0]))){
					HashMap<Integer,Double> movieId = new HashMap<Integer,Double>();
					movieId.put(Integer.parseInt(ar[1]),Double.parseDouble(ar[2]));
					ratings_movie.put(Integer.parseInt(ar[0]),movieId);
				}
				else
				{
					HashMap<Integer,Double> movieId = ratings_movie.get(Integer.parseInt(ar[0]));
					movieId.put(Integer.parseInt(ar[1]),Double.parseDouble(ar[2]));
				}
				
				if(!ratings_user.containsKey(Integer.parseInt(ar[1]))){
					HashMap<Integer,Double> movieId = new HashMap<Integer,Double>();
					movieId.put(Integer.parseInt(ar[0]),Double.parseDouble(ar[2]));
					ratings_user.put(Integer.parseInt(ar[1]),movieId);
				}
				else
				{
					HashMap<Integer,Double> movieId = ratings_user.get(Integer.parseInt(ar[1]));
					movieId.put(Integer.parseInt(ar[0]),Double.parseDouble(ar[2]));
				}
				i++;
					
            }  
			
			for (Map.Entry<Integer,HashMap<Integer,Double>> entry : ratings_user.entrySet()){ 
					double rate=0;
					HashMap<Integer,Double> movieId = entry.getValue();
					for (Map.Entry<Integer,Double> entryInner : movieId.entrySet()) 
					rate+=entryInner.getValue();
				avg_rating.put(entry.getKey(),rate/movieId.size());
					
			}
			
			//System.out.println(i);
			//System.out.println(avg_rating.get(1331154));

			fileReader =   new FileReader(fileName1.trim());

            // Always wrap FileReader in BufferedReader.
            bufferedReader = 
                new BufferedReader(fileReader);
			//System.out.println("Hello");
			long startTime=System.nanoTime();
			double ans1=0;
			double ans2=0;
			int ij=0;
            while((line = bufferedReader.readLine()) != null) {
                String[] ar=line.split(",");
				ij++;
				
				int u=Integer.parseInt(ar[1]);
				int m=Integer.parseInt(ar[0]);
				double r=Double.parseDouble(ar[2]);
				double pro=0;
				double va= avg_rating.get(u); // get average rating of current user
				double extra=0;
				double rhs=0;
				
				HashMap<Integer,Double> movie_rated_list_current = ratings_user.get(u);
				
				// Find the list of users who have rated current movieId

				HashMap<Integer, Double> users_rating_current_movie = ratings_movie.get(m);
				
				// Iterate the list of users
				for (Map.Entry<Integer,Double> user_list : users_rating_current_movie.entrySet()){
					
					double one=0;
					double two=0;
					double three=0;
					double four=0;
					double num=0;
					double den=0;
					double weight=0;
					double rhs_left= user_list.getValue()-avg_rating.get(user_list.getKey());
					
					//Find the list of movies rated by each user
					HashMap<Integer,Double> movie_rated_list = ratings_user.get(user_list.getKey());
					double vj=avg_rating.get(user_list.getKey());
					//Iterate the list of movies
					
					if(movie_rated_list_current.size()>movie_rated_list.size()){
					
					for (Map.Entry<Integer,Double> movie_list : movie_rated_list.entrySet()){
						if(movie_rated_list_current.containsKey(movie_list.getKey()))
						{
							one=movie_rated_list_current.get(movie_list.getKey())-va;
							two=movie_list.getValue()-vj;
							num+=one*two;
							three+= one*one;
							four+=two*two;
							
							//System.out.println(one*two+" "+three+" "+four);
						}
						
					}
					}
					else{
						for (Map.Entry<Integer,Double> movie_list : movie_rated_list_current.entrySet()){
						if(movie_rated_list.containsKey(movie_list.getKey()))
						{
							one=movie_list.getValue()-va;
							two=movie_rated_list.get(movie_list.getKey())-vj;
							num+=one*two;
							three+= one*one;
							four+=two*two;
							
							//System.out.println(one*two+" "+three+" "+four);
						}
						
					}
					}
				den=Math.sqrt(three*four);
				//System.out.println(den);
				if(den!=0){
				weight= num/den;
				extra+=Math.abs(weight);
				rhs+=rhs_left*weight;
				}
					
				}
				if(extra!=0)
				pro=va+rhs/extra;
				
				//System.out.println( pro+" "+r);
				ans1+=(pro-r)*(pro-r);
						
				ans2+=Math.abs(pro-r);
				
				
				if(ij==check)
					break;
			
			if(ij%100==0)
			System.out.println(ij);	
		
			}
			System.out.println("RMS: "+ans1/ij+" Absolute ME: " +ans2/ij);
			
			long endTime=System.nanoTime();
			//System.out.println(TimeUnit.NANOSECONDS.toSeconds(endTime-startTime)); // to calculate the time of execution
	
            // Always close files.
            bufferedReader.close();         
        }
        catch(FileNotFoundException ex) {
            System.out.println(
                "Unable to open file '" + 
                fileName + "'");                
        }
        catch(IOException ex) {
            System.out.println(
                "Error reading file '" 
                + fileName + "'");                  
            // Or we could just do this: 
            // ex.printStackTrace();
        }
    }
}
