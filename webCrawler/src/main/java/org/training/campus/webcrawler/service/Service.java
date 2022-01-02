package org.training.campus.webcrawler.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.TemporalUnit;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.ServletException;

import org.training.campus.webcrawler.dao.JpaDao;
import org.training.campus.webcrawler.service.data.DomainData;
import org.training.campus.webcrawler.service.data.UriEntry;

public final class Service implements AutoCloseable {

	private final Crawler crawler;

	private Service() {
		crawler = new Crawler();
	}

	private static class Holder {
		private static Service instance = new Service();
	}

	public static Service getInstance() {
		return Holder.instance;
	}

	public void init(String url, String user, String password) {
		JpaDao.getInstance().init(url, user, password);
	}

	@Override
	public void close() {
		JpaDao.getInstance().close();
	}

	public DomainData packDomainData(String startPage, long maxVisitedUris, long elapsedTime,
			SortedSet<UriEntry> uriEntries) {
		long linkCount = uriEntries.stream().count();
		int maxDistance = uriEntries.stream().mapToInt(UriEntry::getDistance).max().orElseGet(() -> 0);
		long externalLinksCount = uriEntries.stream().mapToLong(UriEntry::getExternalLinksCount).sum();
		return new DomainData(startPage, maxVisitedUris, linkCount, maxDistance, externalLinksCount, elapsedTime,
				uriEntries);
	}

	public SortedSet<UriEntry> sortBy(SortedSet<UriEntry> uriEntries, Comparator<UriEntry> comparator) {
		var sortedEntries = new TreeSet<UriEntry>(comparator);
		sortedEntries.addAll(uriEntries);
		return sortedEntries;
	}

	public SortedSet<UriEntry> fetchData(URI startUri, long maxVisitedUris) {
		return crawler.crawl(startUri, maxVisitedUris);
	}

	public void saveData(DomainData domainData) {
		JpaDao.getInstance().saveLinksDomainData(domainData);
	}

	public DomainData collectData(String startPage, long maxVisitedUris) throws ServletException {
		try {
			LocalDateTime start = LocalDateTime.now();
			var uriEntries = fetchData(new URI(startPage), maxVisitedUris);
			LocalDateTime finish = LocalDateTime.now();
			long elapsedTime = Duration.between(start, finish).toSeconds();
			var collectedLinks = sortBy(uriEntries, UriEntry.DISTANCE_COMPARATOR);
			return packDomainData(startPage, maxVisitedUris, elapsedTime, collectedLinks);
		} catch (URISyntaxException e) {
			throw new ServletException(e);
		}
	}

}
