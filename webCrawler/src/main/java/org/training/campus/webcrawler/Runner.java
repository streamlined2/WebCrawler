package org.training.campus.webcrawler;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.SortedSet;
import org.training.campus.webcrawler.data.UriEntry;

public class Runner {
	
	private static final String startPage = "https://www.york.ac.uk/teaching/cws/wws/webpage1.html";
	private static final int MAX_VISITED_URIS = 300;

	public static void main(String[] args) {

		try {
			var crawler = new Crawler(new URI(startPage), MAX_VISITED_URIS);
			SortedSet<UriEntry> uriEntries = crawler.process(UriEntry.DISTANCE_COMPARATOR);
			uriEntries.forEach(System.out::println);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

	}

}
