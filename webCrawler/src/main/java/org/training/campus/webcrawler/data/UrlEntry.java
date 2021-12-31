package org.training.campus.webcrawler.data;

import java.net.URL;
import java.util.Objects;
import java.util.StringJoiner;

public record UrlEntry(URL url, int distance, URL parentUrl) {

	public UrlEntry {
		Objects.requireNonNull(url, "url value shouldn't be null");
		Objects.requireNonNull(url, "parent url value shouldn't be null");
		if (distance < 0)
			throw new IllegalArgumentException("negative distance is wrong value");
	}

	@Override
	public String toString() {
		return new StringJoiner(",", "[", "]").add(url.toString()).add(String.valueOf(distance))
				.add(parentUrl.toString()).toString();
	}

}
