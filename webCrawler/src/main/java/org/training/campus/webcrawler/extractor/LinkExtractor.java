package org.training.campus.webcrawler.extractor;

import java.net.MalformedURLException;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.training.campus.webcrawler.data.UrlCollection;
import org.training.campus.webcrawler.data.UrlEntry;

public class LinkExtractor {

	private static final String LINK_QUERY = "a[href]";
	private static final String REF_ATTRIBUTE = "href";

	public UrlCollection process(String html, int parentDistance, URL parentUrl) {
		Document doc = Jsoup.parse(html);
		Elements links = doc.select(LINK_QUERY);
		UrlCollection urlCollection = new UrlCollection();
		links.forEach(link -> {
			try {
				urlCollection.add(new UrlEntry(new URL(parentUrl, link.attributes().get(REF_ATTRIBUTE)),
						parentDistance + 1, parentUrl));
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		});
		return urlCollection;
	}

}
