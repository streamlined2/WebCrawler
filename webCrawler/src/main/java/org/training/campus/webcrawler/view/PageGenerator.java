package org.training.campus.webcrawler.view;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import freemarker.template.Version;

public class PageGenerator {
	private static final String TEMPLATE_FOLDER = "/templates";

	private final Configuration cfg;

	public static PageGenerator instance() {
		return Holder.INSTANCE;
	}

	private static class Holder {
		private static final PageGenerator INSTANCE;
		static {
			try {
				INSTANCE = new PageGenerator();
			} catch (Exception e) {
				throw new InstantiationError(String.format(
						"can't instantiate page generator because template folder is invalid, %s", e.getMessage()));
			}
		}
	}

	public String getPage(String filename, Map<String, Object> data) {
		try {
			Writer stream = new StringWriter();
			cfg.getTemplate(filename).process(data, stream);
			return stream.toString();
		} catch (IOException | TemplateException e) {
			throw new ViewException(e);
		}
	}

	private PageGenerator() throws IOException {
		cfg = new Configuration(new Version(2, 3, 31));
		cfg.setClassForTemplateLoading(getClass(), TEMPLATE_FOLDER);
	}
}
