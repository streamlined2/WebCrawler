package org.training.campus.webcrawler.data;

import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class UrlCollection implements Iterable<UrlEntry> {

	private final Map<URL, UrlEntry> map;

	public UrlCollection() {
		map = new HashMap<>();
	}

	public boolean add(UrlEntry urlEntry) {
		Objects.requireNonNull(urlEntry, "url entry shouldn't be null");
		boolean containsEntry = map.containsKey(urlEntry.url());
		map.put(urlEntry.url(), urlEntry);
		return !containsEntry;
	}

	@Override
	public String toString() {
		return map.values().stream().map(UrlEntry::toString).collect(Collectors.joining("\n"));
	}

	@Override
	public Iterator<UrlEntry> iterator() {
		return map.values().iterator();
	}

}
