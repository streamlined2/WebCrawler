package org.training.campus.webcrawler.fetcher;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpClient.Version;
import java.time.Duration;

public class Fetcher {

	private static final Duration HTTP_TIMEOUT = Duration.ofSeconds(20);

	private final HttpClient httpClient;

	public Fetcher() {
		httpClient = HttpClient.newBuilder().version(Version.HTTP_2).followRedirects(Redirect.NORMAL)
				.connectTimeout(HTTP_TIMEOUT).build();
	}

	public HttpResponse<String> fetch(URL url) throws IOException, InterruptedException, URISyntaxException {
		HttpRequest request = HttpRequest.newBuilder().GET().uri(url.toURI()).setHeader("User-Agent", "WebCrawler")
				.build();
		return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
	}

}
