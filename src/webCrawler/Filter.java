package webCrawler;

public class Filter {
	
	public static LinkFilter linkFilter = new LinkFilter(){
		public boolean accept(String url){
			if(url.startsWith("/fakebook")){
				return true;
			}else{
				return false;
			}
		}			
	};

	public static void main(String[] args) {

	}

}
