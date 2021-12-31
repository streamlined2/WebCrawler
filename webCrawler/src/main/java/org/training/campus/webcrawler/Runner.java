package org.training.campus.webcrawler;

import java.io.IOException;
import java.net.http.HttpResponse;

import org.training.campus.webcrawler.data.UrlCollection;
import org.training.campus.webcrawler.extractor.Extractor;
import org.training.campus.webcrawler.fetcher.Fetcher;

public class Runner {

	private static final String URL = "https://www.york.ac.uk/teaching/cws/wws/webpage2.html";

	public static void main(String[] args) throws IOException, InterruptedException {

		Fetcher fetcher = new Fetcher();
		HttpResponse<String> response = fetcher.fetch(URL);

		var urlCollection = new UrlCollection();
		var extractor = new Extractor(urlCollection);
		extractor.process(response.body(), 0, URL);
		System.out.println(urlCollection);

	}

}
