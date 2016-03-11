package webCrawler;

import java.util.Iterator;

public class HtmlMethod {
	
	private static String httpVersion = "HTTP/1.1";
	private static String userAgent = "Mozilla/5.0 (Windows NT 6.2; WOW64) " 
			+"AppleWebKit/537.36 (KHTML, like Gecko) Chrome/35.0.1916.153 Safari/537.36 SE 2.X MetaSr 1.0";
	private static String contentType = "application/x-www-form-urlencoded";
	
	public static String getMethod(String host, String localPath){
		
		String getMessage = "GET " + localPath + " " + httpVersion + "\r\n";
		getMessage += "Host: " + host + "\r\n";	
		if(!WebCrawler.cookie.isEmpty()){
			getMessage += "Cookie: ";
			Iterator<String> iterator = WebCrawler.cookie.keySet().iterator();
			while(iterator.hasNext()){
				String key = (String)iterator.next();
				String value = WebCrawler.cookie.get(key);
				getMessage += key + "=" + value + "; ";
			}
			getMessage += "\r\n";
		}
		
		getMessage += "\r\n";
		
//		System.out.println(getMessage);
		
		return getMessage;
		
	} 
	
	public static String postMethod(String host, String localPath, String username, String password){
		
		String postMessage = "POST " + localPath + " " + httpVersion + "\r\n";
		postMessage += "Host: " + host + "\r\n";
		postMessage += "User-Agent: " + userAgent + "\r\n";
		postMessage += "Content-Type: " + contentType + "\r\n";
		postMessage += "Content-length: 100\r\n";
		
		if(!WebCrawler.cookie.isEmpty()){
			Iterator<String> iterator = WebCrawler.cookie.keySet().iterator();
			postMessage += "Cookie: ";
			while(iterator.hasNext()){
				String key = (String)iterator.next();
				String value = WebCrawler.cookie.get(key);
				System.out.println(key + " " + value);
				postMessage += key + "=" + value + "; ";
			}
		}
		
		postMessage += "\r\n\r\n";
		postMessage += "username=" + username + "&" + "password=" + password + 
				"&" + "csrfmiddlewaretoken=" + WebCrawler.cookie.get("csrftoken") + 
				"&" + "next=%2Ffakebook%2F";
		
		System.out.println(postMessage);
		
		return postMessage;
	}
	
//	public static void dealWithStatusCode(String statusCode, String url){
//		switch(statusCode){
//		case "500": SocketClient.getRequest(url); 
//		}
//		
//	}

	public static void main(String[] args) {
//		WebCrawler.cookie.put("csrftoken", "412b685a51fc7eceaa3c3038c60fb55b");
//		WebCrawler.cookie.put("sessionid", "7d97c85fe1a70ddda3e3c8e707a48ed7");
//		String s = postMethod("cs5700sp15.ccs.neu.edu", "/accounts/login/", "001971981", "F3C2GRNL");
//		String s = getMethod("www.dotamax.com", "/hero/");
//		System.out.println(s);
	}

}
