package webCrawler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class SocketClient {

	/*
	 * GET Request
	 */

	public static void getRequest(String url) throws IOException {

		Socket clientSocket = null;
		PrintWriter clientSocketOutput = null;
		BufferedReader clientSocketInput = null;
		int portNum = 80;

		url = url.substring(7);
		String localPath = url.substring(url.indexOf("/"));
		String host = url.substring(0, url.indexOf("/"));

		Pattern pattern;
		Matcher matcher;
		Pattern keyValuePairPattern;
		Matcher keyValuePairMatcher;
		Pattern redirPattern;
		Matcher redirMatcher;
		Pattern statusPattern;
		Matcher statusMatcher;
		
		String messageStorage = null;
		String key = null;
		String value = null;
		String statusCode = null;
		String fakebookLink = null;

		try {
			clientSocket = new Socket(host, portNum);
			clientSocketOutput = new PrintWriter(
					clientSocket.getOutputStream(), true);
			clientSocketInput = new BufferedReader(new InputStreamReader(
					clientSocket.getInputStream()));
		} catch (UnknownHostException e) {
			System.err.println("Doesn't know about this host.");
			System.exit(1);
		} catch (ConnectException e){
			getRequest(WebCrawler.visitingUrl);
			return;
		}

		
		String getRequestMessage = HtmlMethod.getMethod(host, localPath);
		clientSocketOutput.println(getRequestMessage);
		
//		Timer timer = new Timer(5000);
//		timer.start();

		String responseMessage;
		if((responseMessage = clientSocketInput.readLine()) != null){
			
			statusPattern = Pattern.compile("[0-9]{3}");
			statusMatcher = statusPattern.matcher(responseMessage);
			if(statusMatcher.find()){
				statusCode = statusMatcher.group(0);
			}
			messageStorage = responseMessage + "\r\n";
 
			
			while((responseMessage = clientSocketInput.readLine()) != null){
				
				System.out.println(responseMessage);
				
				messageStorage += responseMessage + "\r\n";				
				if (responseMessage.startsWith("Set-Cookie:")) {					
					pattern = Pattern.compile("\\w+=\\w+");
					matcher = pattern.matcher(responseMessage);					
					if (matcher.find()) {
						String keyValuePair = matcher.group(0);
						keyValuePairPattern = Pattern.compile("\\w+");
						keyValuePairMatcher = keyValuePairPattern.matcher(keyValuePair);
						
						if (keyValuePairMatcher.find()) {
							key = keyValuePairMatcher.group(0);
						}
						if (keyValuePairMatcher.find()) {
							value = keyValuePairMatcher.group(0);
						}						
						WebCrawler.cookie.put(key, value);
					}
				}
			}
			
//			timer.resetElapsedTime();
			
			switch(statusCode){
			
			case "200": break;
			case "301": redirPattern = Pattern.compile("(Location: )([.]+)");
						redirMatcher = redirPattern.matcher(messageStorage);
						if(redirMatcher.find()){
							WebCrawler.visitingUrl = redirMatcher.group(2);
							getRequest(WebCrawler.visitingUrl);
						}
						return;
			case "403": UrlQueue.addErrorUrl(WebCrawler.visitingUrl);
						break;
			case "404": UrlQueue.addErrorUrl(WebCrawler.visitingUrl);
						break;
			case "500": getRequest(WebCrawler.visitingUrl);
						return; 
			default: System.out.println("Unknown status code." + statusCode);
					 break;
					 
			}
			
			
			Document html = Jsoup.parse(messageStorage);
			Elements links = html.select("a[href]");
			for(Element link : links){
				if(Filter.linkFilter.accept(link.attr("href"))){
					fakebookLink = "http://cs5700sp15.ccs.neu.edu" + link.attr("href");
					UrlQueue.addUnvisitedUrl(fakebookLink);
				}
			}
			
			Elements secretFlags = html.select("h2[class='secret_flag']");
			for(Element secretFlag : secretFlags){
				WebCrawler.secretFlags.add(secretFlag.text());
			}
		}

		clientSocketOutput.close();
		clientSocketInput.close();
		clientSocket.close();
	}

	
	/*
	 * POST Request
	 */

	public static void postRequest(String url, String username, String password) throws IOException{
		
		Socket clientSocket = null;
		PrintWriter clientSocketOutput = null;
		BufferedReader clientSocketInput = null;
		int portNum = 80;
		
		Pattern pattern;
		Matcher matcher;
		Pattern statusPattern;
		Matcher statusMatcher;
		Pattern redirPattern;
		Matcher redirMatcher;
		Pattern keyValuePairPattern;
		Matcher keyValuePairMatcher;
		String messageStorage = null;
		String key = null;
        String value = null;
        String statusCode = null;
		
		String newUrl = url.substring(7);
		String host = newUrl.substring(0, newUrl.indexOf("/"));
		String localPath = newUrl.substring(newUrl.indexOf("/"));
		
		try{
			clientSocket = new Socket(host, portNum);
			clientSocketOutput = new PrintWriter(clientSocket.getOutputStream(), true);
			clientSocketInput = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		}catch(UnknownHostException e){
			System.err.println("Don't know about host");
            System.exit(1);
		}catch (ConnectException e){
			getRequest(WebCrawler.visitingUrl);
		}
		
		String postMessage = HtmlMethod.postMethod(host, localPath, username, password);		
		clientSocketOutput.println(postMessage);
		
		String responseMessage;
		
//		Timer timer = new Timer(5000);
//		timer.start();
		
		if((responseMessage = clientSocketInput.readLine()) != null){
			statusPattern = Pattern.compile("[0-9]{3}");
			statusMatcher = statusPattern.matcher(responseMessage);
			if(statusMatcher.find()){
				statusCode = statusMatcher.group(0);
			}
			messageStorage = responseMessage;
			System.out.println(messageStorage);
			System.out.println("Status Code: " + statusCode);
			while((responseMessage = clientSocketInput.readLine()) != null){
				messageStorage += responseMessage;

				if (responseMessage.startsWith("Set-Cookie:")) {					
					pattern = Pattern.compile("\\w+=\\w+");
					matcher = pattern.matcher(responseMessage);					
					if (matcher.find()) {
						String keyValuePair = matcher.group(0);
						keyValuePairPattern = Pattern.compile("\\w+");
						keyValuePairMatcher = keyValuePairPattern.matcher(keyValuePair);
						
						if (keyValuePairMatcher.find()) {
							key = keyValuePairMatcher.group(0);
						}
						if (keyValuePairMatcher.find()) {
							value = keyValuePairMatcher.group(0);
						}						
						WebCrawler.cookie.put(key, value);
					}
				}
			}
			
//			timer.resetElapsedTime();
			
			switch(statusCode){
			
			case "200": break;
			case "301": redirPattern = Pattern.compile("(Location: )([.]+)");
						redirMatcher = redirPattern.matcher(messageStorage);
						if(redirMatcher.find()){
							WebCrawler.visitingUrl = redirMatcher.group(2);
							getRequest(WebCrawler.visitingUrl);
						}
						return;
			case "403": UrlQueue.addErrorUrl(WebCrawler.visitingUrl);
						break;
			case "404": UrlQueue.addErrorUrl(WebCrawler.visitingUrl);
						break;
			case "500": getRequest(WebCrawler.visitingUrl);
						return; 
			default: System.out.println("Unknown status code: " + statusCode);
					 break;
					 
			}
		}
		
		
		
		clientSocketOutput.close();
		clientSocketInput.close();
		clientSocket.close();
	}
	

	public static void main(String args[]) throws IOException {

		// SocketClient.getRequest("http://cs5700sp15.ccs.neu.edu/accounts/login/?next=/fakebook/");
	}

}
