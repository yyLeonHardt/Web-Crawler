package webCrawler;

import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

public class UrlQueue {
	
	private static Queue<String> unvisitedUrl = new PriorityQueue<String>();
	private static Set<String> visitedUrl = new HashSet<String>();
	private static Set<String> errorUrl = new HashSet<String>();

	public static Queue<String> getUnvisitedUrl() {
		return unvisitedUrl;
	}

	public static void setUnvisitedUrl(Queue<String> unvisitedUrl) {
		UrlQueue.unvisitedUrl = unvisitedUrl;
	}
	
	public static Set<String> getVisitedUrl() {
		return visitedUrl;
	}

	public static void setVisitedUrl(Set<String> visitedUrl) {
		UrlQueue.visitedUrl = visitedUrl;
	}

	public static Set<String> getErrorUrl() {
		return errorUrl;
	}

	public static void setErrorUrl(Set<String> errorUrl) {
		UrlQueue.errorUrl = errorUrl;
	}

	public static void addUnvisitedUrl(String url){
		if(url != null && !url.trim().equals("") && 
				!unvisitedUrl.contains(url) && !visitedUrl.contains(url)
				&& !errorUrl.contains(url)){
			unvisitedUrl.add(url);
		}
	}
	
	public static boolean unvisitedUrlIsEmpty(){
		return unvisitedUrl.isEmpty();
	}
	
	public static String unvisitedUrlPoll(){
		return unvisitedUrl.poll();
	}
	
	public static void addVisitedUrl(String url){
		visitedUrl.add(url);
	}
	
	public static void addErrorUrl(String url){
		errorUrl.add(url);
	}
	
	public static void initQueueForUrl(String[] url){
		for(int i = 0; i < url.length; i++){
			addUnvisitedUrl(url[i]);
		}
		
	}

	public static void main(String[] args) {

	}

}
