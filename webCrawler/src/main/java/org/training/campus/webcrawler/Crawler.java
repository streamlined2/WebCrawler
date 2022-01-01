package org.training.campus.webcrawler;

import java.net.URI;
import java.net.http.HttpResponse;
import java.util.Comparator;
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

	public SortedSet<UriEntry> process(Comparator<UriEntry> comparator) {
		SortedSet<UriEntry> visitedUris = new TreeSet<>(UriEntry.URI_COMPARATOR);
		Queue<UriEntry> uriQueue = new LinkedList<>();
		uriQueue.add(new UriEntry(startUri, 0, null));
		while (!uriQueue.isEmpty() && visitedUris.size() <= maxVisitedUris) {
			UriEntry uriEntry = uriQueue.poll();
			if (!visitedUris.contains(uriEntry)) {
				HttpResponse<String> response = fetcher.fetch(uriEntry.uri());
				if (response != null) {
					UriCollection uriCollection = linkExtractor.process(response.body(), uriEntry.distance(),
							uriEntry.uri());
					uriCollection.internalLinksStream(startUri).forEach(uriQueue::add);
					visitedUris.add(uriEntry);
				}
			}
		}
		var sortedEntries = new TreeSet<UriEntry>(comparator);
		sortedEntries.addAll(visitedUris);
		return sortedEntries;
	}

}
