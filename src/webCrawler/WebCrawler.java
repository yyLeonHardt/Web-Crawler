package webCrawler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class WebCrawler {
	
	public static HashMap<String, String> cookie = new HashMap<String, String>();
	public static String visitingUrl = null;
	public static Set<String> secretFlags = new HashSet<String>();
	

	
	public void crawler(String username, String password) throws IOException{
		
		File visitedUrls = new File("H:/l留学/k课程/2015.1-2015.4/CS5700_FundamentalComputerNetworking/Project/Project2/visitedUrls.txt");
		File errorUrls = new File("H:/l留学/k课程/2015.1-2015.4/CS5700_FundamentalComputerNetworking/Project/Project2/errorUrls.txt");
		File secret_flags = new File("H:/l留学/k课程/2015.1-2015.4/CS5700_FundamentalComputerNetworking/Project/Project2/secretFlags.txt");
		FileWriter fileWriter;
		BufferedWriter bufferedWriter;
		
		String[] seeds = {"http://cs5700sp15.ccs.neu.edu/fakebook/"};
		
		SocketClient.getRequest("http://cs5700sp15.ccs.neu.edu/accounts/login/?next=/fakebook/");
		SocketClient.postRequest("http://cs5700sp15.ccs.neu.edu/accounts/login/", username, password);

		UrlQueue.initQueueForUrl(seeds);

		while(!UrlQueue.unvisitedUrlIsEmpty()){
			visitingUrl = UrlQueue.unvisitedUrlPoll();
			SocketClient.getRequest(visitingUrl);
			UrlQueue.addVisitedUrl(visitingUrl);
		}
		
		System.out.println(secretFlags);
		
		try{
			fileWriter = new FileWriter(visitedUrls);
			bufferedWriter = new BufferedWriter(fileWriter);
			bufferedWriter.write(UrlQueue.getVisitedUrl().toString());
			
			fileWriter = new FileWriter(errorUrls);
			bufferedWriter = new BufferedWriter(fileWriter);
			bufferedWriter.write(UrlQueue.getErrorUrl().toString());
			
			fileWriter = new FileWriter(secret_flags);
			bufferedWriter = new BufferedWriter(fileWriter);
			bufferedWriter.write(secretFlags.toString());
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		
		
	}

	public static void main(String[] args) throws IOException {
		System.out.println("Please enter username and password to start the program.");
		Scanner input = new Scanner(System.in);
		input.next();
		String username = input.next();
		String password = input.next();
		input.close();
		
		WebCrawler webCrawler = new WebCrawler();
		webCrawler.crawler(username, password);


	}

}
