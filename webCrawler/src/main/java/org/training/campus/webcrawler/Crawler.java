package org.training.campus.webcrawler;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpResponse;
import java.util.LinkedList;
import java.util.Queue;
import java.util.SortedSet;
import java.util.TreeSet;

import org.training.campus.webcrawler.data.UriCollection;
import org.training.campus.webcrawler.data.UriEntry;
import org.training.campus.webcrawler.extractor.LinkExtractor;
import org.training.campus.webcrawler.fetcher.Fetcher;

public class Crawler {

	private final URI startUri;
	private final int maxVisitedUris;
	private final Fetcher fetcher;
	private final LinkExtractor linkExtractor;

	public Crawler(URI uri, int maxVisitedUris) {
		this.startUri = uri;
		this.maxVisitedUris = maxVisitedUris;
		fetcher = new Fetcher();
		linkExtractor = new LinkExtractor();
	}

	public SortedSet<UriEntry> crawl() {
		try {
			SortedSet<UriEntry> visitedUris = new TreeSet<>(UriEntry.URI_COMPARATOR);
			Queue<UriEntry> uriQueue = new LinkedList<>();
			uriQueue.add(new UriEntry(startUri, 0, null));
			while (!uriQueue.isEmpty() && visitedUris.size() < maxVisitedUris) {
				UriEntry uriEntry = uriQueue.poll();
				if (!visitedUris.contains(uriEntry)) {
					HttpResponse<String> response = fetcher.fetch(uriEntry.getUri());
					if (response != null) {
						UriCollection uriCollection = linkExtractor.extract(response.body(), uriEntry);
						uriCollection.internalLinksStream(startUri).forEach(uriQueue::add);
						uriEntry.setExternalLinksCount(uriCollection.externalLinksCount(startUri));
						visitedUris.add(uriEntry);
					}
				}
			}
			return visitedUris;
		} catch (URISyntaxException e) {
			throw new NetException(e);
		}
	}

}
