package org.training.campus.webcrawler.extractor;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.training.campus.webcrawler.data.UrlCollection;
import org.training.campus.webcrawler.data.UrlEntry;

public class Extractor {

	private static final String LINK_QUERY = "a[href]";
	private static final String REF_ATTRIBUTE = "href";

	private final UrlCollection urlCollection;

	public Extractor(UrlCollection urlCollection) {
		this.urlCollection = urlCollection;
	}

	public void process(String messageBody, int parentDistance, String parentUrl) {
		Document doc = Jsoup.parse(messageBody);
		Elements links = doc.select(LINK_QUERY);
		links.forEach(link -> {
			urlCollection.add(new UrlEntry(link.attributes().get(REF_ATTRIBUTE), parentDistance + 1, parentUrl));
		});
	}

}
