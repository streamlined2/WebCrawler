package org.training.campus.webcrawler.data;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Comparator;
import java.util.Objects;
import java.util.StringJoiner;

public record UriEntry(URI uri, int distance, URI parentUri) {
	
	public static final Comparator<UriEntry> URI_COMPARATOR = Comparator.comparing(UriEntry::noFragmentUri);
	public static final Comparator<UriEntry> DISTANCE_COMPARATOR = Comparator.comparing(UriEntry::distance).thenComparing(UriEntry::noFragmentUri);

	public UriEntry {
		Objects.requireNonNull(uri, "uri value shouldn't be null");
		if (distance < 0)
			throw new IllegalArgumentException("negative distance is wrong value");
	}
	
	public URI noFragmentUri() {
		try {
			return new URI(uri().getScheme(),uri.getUserInfo(),uri.getHost(),uri.getPort(),uri.getPath(),uri.getQuery(),"");
		} catch (URISyntaxException e) {
			//impossible to occur because given uri() is known to be correct
		}
		return null;
	}

	@Override
	public String toString() {
		return new StringJoiner(",", "[", "]").add(String.valueOf(uri)).add(String.valueOf(distance))
				.add(String.valueOf(parentUri)).toString();
	}

	public static String getDomainName(URI uri) {
		String domainName = uri.getHost();
		if (domainName != null) {
			return domainName.startsWith("www.") ? domainName.substring(4) : domainName;
		}
		return null;
	}

}
