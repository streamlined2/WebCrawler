package org.training.campus.webcrawler.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpClient.Version;
import java.time.Duration;

public class Fetcher {

	private static final String USER_AGENT = "WebCrawler";
	private static final Duration HTTP_TIMEOUT = Duration.ofSeconds(20);

	private final HttpClient httpClient;

	public Fetcher() {
		httpClient = HttpClient.newBuilder().version(Version.HTTP_2).followRedirects(Redirect.NORMAL)
				.connectTimeout(HTTP_TIMEOUT).build();
	}

	public HttpResponse<String> fetch(URI uri) {
		try {
			var request = HttpRequest.newBuilder().GET().uri(uri).setHeader("User-Agent", USER_AGENT).build();
			return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
		} catch (IOException | InterruptedException e) {
			throw new NetException(e);
		} catch(IllegalArgumentException e2) {
			//malformed URIs are to be quietly skipped
		}
		return null;
	}

}
