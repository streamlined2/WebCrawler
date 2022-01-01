package org.training.campus.webcrawler.data;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Comparator;
import java.util.Objects;
import java.util.StringJoiner;

public class UriEntry implements Comparable<UriEntry> {

	public static final Comparator<UriEntry> URI_COMPARATOR = Comparator.comparing(UriEntry::getUri);
	public static final Comparator<UriEntry> DISTANCE_COMPARATOR = Comparator.comparing(UriEntry::getDistance)
			.thenComparing(UriEntry::getUri);

	private final URI uri;
	private final int distance;
	private final URI parentUri;
	private long externalLinksCount;

	public UriEntry(URI uri, int distance, URI parentUri) throws URISyntaxException {
		Objects.requireNonNull(uri, "uri value shouldn't be null");
		if (distance < 0)
			throw new IllegalArgumentException("negative distance is wrong value");
		this.uri = noFragmentUri(uri);
		this.distance = distance;
		this.parentUri = parentUri;
	}

	public URI getUri() {
		return uri;
	}

	public int getDistance() {
		return distance;
	}

	public URI getParentUri() {
		return parentUri;
	}

	public long getExternalLinksCount() {
		return externalLinksCount;
	}

	public void setExternalLinksCount(long externalLinksCount) {
		if (externalLinksCount < 0)
			throw new IllegalArgumentException("negative count of external links is wrong");
		this.externalLinksCount = externalLinksCount;
	}

	public static URI noFragmentUri(URI uri) throws URISyntaxException {
		return new URI(uri.getScheme(), uri.getUserInfo(), uri.getHost(), uri.getPort(), uri.getPath(), uri.getQuery(),
				null);
	}

	public static String getDomainName(URI uri) {
		String domainName = uri.getHost();
		if (domainName != null) {
			return domainName.startsWith("www.") ? domainName.substring(4) : domainName;
		}
		return null;
	}

	@Override
	public String toString() {
		return new StringJoiner(",", "[", "]").add(String.valueOf(uri)).add(String.valueOf(externalLinksCount))
				.add(String.valueOf(distance)).add(String.valueOf(parentUri)).toString();
	}

	@Override
	public int compareTo(UriEntry e) {
		return uri.compareTo(e.uri);
	}

	@Override
	public int hashCode() {
		return uri.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof UriEntry e) {
			return compareTo(e) == 0;
		}
		return false;
	}

}
