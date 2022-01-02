package org.training.campus.webcrawler.service;

import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

import org.training.campus.webcrawler.collector.data.UriEntry;

public class InfoService {

	public static record DomainData(long linkCount, int maxDistance, long externalLinksCount) {

	}

	public static DomainData collectDomainStatisticalInfo(SortedSet<UriEntry> uriEntries) {
		long linkCount = uriEntries.stream().count();
		int maxDistance = uriEntries.stream().mapToInt(UriEntry::getDistance).max().orElseGet(() -> 0);
		long externalLinksCount = uriEntries.stream().mapToLong(UriEntry::getExternalLinksCount).sum();
		return new DomainData(linkCount, maxDistance, externalLinksCount);
	}

	public static SortedSet<UriEntry> sortBy(SortedSet<UriEntry> uriEntries, Comparator<UriEntry> comparator) {
		var sortedEntries = new TreeSet<UriEntry>(comparator);
		sortedEntries.addAll(uriEntries);
		return sortedEntries;
	}

}
