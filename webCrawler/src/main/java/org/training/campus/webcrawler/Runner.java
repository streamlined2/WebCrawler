package org.training.campus.webcrawler;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpResponse;

import org.training.campus.webcrawler.extractor.LinkExtractor;
import org.training.campus.webcrawler.fetcher.Fetcher;

public class Runner {

	public static void main(String[] args) {
		
		try {
			URL pageURL = new URL("https://www.york.ac.uk/teaching/cws/wws/webpage2.html");
			HttpResponse<String> response = new Fetcher().fetch(pageURL);
			System.out.println(new LinkExtractor().process(response.body(), 0, pageURL));
		} catch (IOException | InterruptedException | URISyntaxException e) {
			e.printStackTrace();
		}

	}

}
