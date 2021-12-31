package org.training.campus.webcrawler;

import java.io.IOException;
import java.net.http.HttpResponse;

public class Runner {

	private static final String URL = "https://www.york.ac.uk/teaching/cws/wws/webpage1.html";

	public static void main(String[] args) throws IOException, InterruptedException {

		Fetcher fetcher = new Fetcher();
		HttpResponse response = fetcher.fetch(URL);

		System.out.println("Headers:");
		response.headers().map().forEach((k, v) -> System.out.println(k + "=" + v));

		System.out.println("\nStatus code: "+response.statusCode());

		System.out.println("Body:");
		System.out.println(response.body());

	}

}
