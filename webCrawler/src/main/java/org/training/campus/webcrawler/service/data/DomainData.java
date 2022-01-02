package org.training.campus.webcrawler.service.data;

import java.util.SortedSet;

public record DomainData(String startPage, long maxVisitedUris, long linkCount, int maxDistance,
		long externalLinksCount, long elapsedTime, SortedSet<UriEntry> uriEntries) {
}
