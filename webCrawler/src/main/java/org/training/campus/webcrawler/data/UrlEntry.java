package org.training.campus.webcrawler.data;

import java.util.Objects;
import java.util.StringJoiner;

public record UrlEntry(String url, int distance, String parentUrl) {

	public UrlEntry {
		Objects.requireNonNull(url, "url value shouldn't be null");
		Objects.requireNonNull(url, "parent url value shouldn't be null");
	}

	@Override
	public String toString() {
		return new StringJoiner(",", "[", "]").add(url).add(String.valueOf(distance)).add(parentUrl).toString();
	}

}
