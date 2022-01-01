package org.training.campus.webcrawler.extractor;

import java.net.URI;
import java.util.Set;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.training.campus.webcrawler.data.UriCollection;
import org.training.campus.webcrawler.data.UriEntry;

public class LinkExtractor {

	private static final String LINK_QUERY = "a[href]";
	private static final String REF_ATTRIBUTE = "href";

	public UriCollection process(String html, int parentDistance, URI parentUri) {
		Document doc = Jsoup.parse(html);
		Elements links = doc.select(LINK_QUERY);
		UriCollection uriCollection = new UriCollection();
		links.forEach(link -> {
			try {
				String attrValue = link.attributes().get(REF_ATTRIBUTE);
				if (attrValue != null) {
					URI nextUri = parentUri.resolve(attrValue.trim());
					if (acceptable(nextUri)) {
						uriCollection.add(new UriEntry(nextUri, parentDistance + 1, parentUri));
					}
				}
			} catch (IllegalArgumentException e) {
				// plenty of malformed references cause detailed logging impossible and should be skipped
			}
		});
		return uriCollection;
	}

	private static final Set<String> unacceptableUriTypes = Set.of("rtf", "pdf", "doc");

	private static boolean acceptable(URI nextUri) {
		for (var type : unacceptableUriTypes) {
			String path = nextUri.getPath();
			if (path != null && path.toLowerCase().endsWith(type)) {
				return false;
			}
		}
		return true;
	}

}
