package org.training.campus.webcrawler.data;

import java.net.URI;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UriCollection implements Iterable<UriEntry> {

	private final Map<URI, UriEntry> map;

	public UriCollection() {
		map = new HashMap<>();
	}

	public boolean add(UriEntry uriEntry) {
		Objects.requireNonNull(uriEntry, "given uri entry shouldn't be null");
		boolean containsEntry = map.containsKey(uriEntry.getUri());
		map.put(uriEntry.getUri(), uriEntry);
		return !containsEntry;
	}

	@Override
	public String toString() {
		return map.values().stream().map(UriEntry::toString).collect(Collectors.joining("\n"));
	}

	@Override
	public Iterator<UriEntry> iterator() {
		return map.values().iterator();
	}

	private static boolean sameDomain(URI uriA, URI uriB) {
		return Objects.equals(UriEntry.getDomainName(uriA), UriEntry.getDomainName(uriB));
	}

	public Stream<UriEntry> internalLinksStream(URI baseUri) {
		return map.values().stream().filter(entry -> sameDomain(entry.getUri(), baseUri));
	}

	public long externalLinksCount(URI baseUri) {
		return map.values().stream().filter(entry -> !sameDomain(entry.getUri(), baseUri)).count();
	}

}
