package org.training.campus.webcrawler;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

import org.training.campus.webcrawler.data.UriEntry;

public class Runner {

	private static final String DEFAULT_START_PAGE = "https://www.york.ac.uk/teaching/cws/wws/webpage1.html";
	private static final int DEFAULT_MAX_VISITED_URIS = 300;

	private static SortedSet<UriEntry> sortBy(SortedSet<UriEntry> uriEntries, Comparator<UriEntry> comparator) {
		var sortedEntries = new TreeSet<UriEntry>(comparator);
		sortedEntries.addAll(uriEntries);
		return sortedEntries;
	}

	private static class DomainInfo {
		private long linkCount;
		private int maxDistance;
		private long externalLinksCount;

		@Override
		public String toString() {
			return String.format(
					"internal domain link count: %d%nmax distance from root: %d%nexternal link count: %d%n", linkCount,
					maxDistance, externalLinksCount);
		}
	}

	private static DomainInfo collectDomainStatisticalInfo(SortedSet<UriEntry> uriEntries) {
		DomainInfo domainInfo = new DomainInfo();
		domainInfo.linkCount = uriEntries.stream().count();
		domainInfo.maxDistance = uriEntries.stream().mapToInt(UriEntry::getDistance).max().orElseGet(() -> 0);
		domainInfo.externalLinksCount = uriEntries.stream().mapToLong(UriEntry::getExternalLinksCount).sum();
		return domainInfo;
	}

	public static void main(String[] args) {

		String startPage = DEFAULT_START_PAGE;
		int maxVisitedUris = DEFAULT_MAX_VISITED_URIS;

		if (args.length > 0) {
			startPage = args[0];

			if (args.length > 1) {
				maxVisitedUris = Integer.parseInt(args[1]);
			}
		}

		try {
			var crawler = new Crawler(new URI(startPage), maxVisitedUris);
			SortedSet<UriEntry> uriEntries = crawler.crawl();

			System.out.println("List of collected links sorted by distance from root page:");
			sortBy(uriEntries, UriEntry.DISTANCE_COMPARATOR).forEach(System.out::println);

			System.out.println("\nDomain statistical information:");
			System.out.println(collectDomainStatisticalInfo(uriEntries));
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

	}

}
