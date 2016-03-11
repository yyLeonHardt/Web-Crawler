package webCrawler;

import java.io.IOException;

public class Timer extends Thread{

	protected int checkRate = 100;
	private int timeoutLength = 5000;
	private int elapsedTime;
	
	public Timer(int timeout_length){
		timeoutLength = timeout_length;
		elapsedTime = 0;
	}
	
	public synchronized void resetElapsedTime(){
		elapsedTime = 0;
	}
	
	public void run(){
		for(;;){
			try{
				Thread.sleep(checkRate);
			}catch(InterruptedException e){
				continue;
			}
			synchronized(this){
				elapsedTime += checkRate;
				if(elapsedTime > timeoutLength){
					retry();
				}
			}
		}
		
	}
	
	public void retry(){
		try {
			SocketClient.getRequest(WebCrawler.visitingUrl);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) {

	}

}
