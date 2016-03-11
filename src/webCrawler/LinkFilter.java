package webCrawler;

public interface LinkFilter {
	public boolean accept(String url);
}
