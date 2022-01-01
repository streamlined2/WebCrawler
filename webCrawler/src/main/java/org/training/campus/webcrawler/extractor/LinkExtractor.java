package org.training.campus.webcrawler.extractor;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.training.campus.webcrawler.data.UriCollection;
import org.training.campus.webcrawler.data.UriEntry;

public class LinkExtractor {

	private static final String LINK_QUERY = "a[href]";
	private static final String REF_ATTRIBUTE = "href";

	private static final Set<String> UNACCEPTABLE_URI_TYPES = Set.of("rtf", "pdf", "doc");

	public UriCollection extract(String html, UriEntry uriEntry) {
		Document doc = Jsoup.parse(html);
		Elements links = doc.select(LINK_QUERY);
		UriCollection uriCollection = new UriCollection();
		links.forEach(link -> {
			try {
				String attrValue = link.attributes().get(REF_ATTRIBUTE);
				if (attrValue != null) {
					URI nextUri = uriEntry.getUri().resolve(attrValue.trim());
					if (acceptable(nextUri)) {
						uriCollection.add(new UriEntry(nextUri, uriEntry.getDistance() + 1, uriEntry.getUri()));
					}
				}
			} catch (IllegalArgumentException | URISyntaxException e) {
				// ignore malformed links at present
			}
		});
		return uriCollection;
	}

	private static boolean acceptable(URI nextUri) {
		for (var type : UNACCEPTABLE_URI_TYPES) {
			String path = nextUri.getPath();
			if (path != null && path.toLowerCase().endsWith(type)) {
				return false;
			}
		}
		return true;
	}

}
