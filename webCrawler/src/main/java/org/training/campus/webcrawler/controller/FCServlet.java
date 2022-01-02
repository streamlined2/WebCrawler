package org.training.campus.webcrawler.controller;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.training.campus.webcrawler.collector.Crawler;
import org.training.campus.webcrawler.collector.data.UriEntry;
import org.training.campus.webcrawler.service.InfoService;
import org.training.campus.webcrawler.view.PageGenerator;

public class FCServlet extends HttpServlet {

	public static final String START_PAGE_PARAMETER = "startPage";
	public static final String MAX_VISITED_URIS_PARAMETER = "maxVisitedUris";
	public static final String JDBC_URL_PARAMETER = "jdbcUrl";
	public static final String JDBC_USER_PARAMETER = "jdbcUser";
	public static final String JDBC_PASSWORD_PARAMETER = "jdbcPassword";

	protected static final String TEMPLATE_PARAMETERS_ATTRIBUTE = "parameters";
	protected static final String CONTEXT_PATH_ATTRIBUTE = "context";
	protected static final String COLLECTED_LINKS_ATTRIBUTE = "collectedLinks";
	protected static final String DOMAIN_STATISTICAL_INFO_ATTRIBUTE = "domainStatInfo";

	private static final String DEFAULT_MAX_VISITED_URIS = "10";

	private final Crawler crawler;

	public FCServlet() {
		crawler = new Crawler();
	}

	@Override
	public void init() throws ServletException {
		super.init();
		setTemplateParameter(START_PAGE_PARAMETER, getServletContext().getInitParameter(START_PAGE_PARAMETER));
		setTemplateParameter(MAX_VISITED_URIS_PARAMETER,
				getServletContext().getInitParameter(MAX_VISITED_URIS_PARAMETER));
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		setTemplateParameter(CONTEXT_PATH_ATTRIBUTE, getServletContext().getContextPath());
		resp.setContentType("text/html;charset=UTF-8");
		resp.setStatus(HttpServletResponse.SC_OK);
		doWork(req);
		resp.getWriter().append(PageGenerator.instance().getPage(getRedirectionResource(), getTemplateParameters()));
	}

	private void doWork(HttpServletRequest req) throws ServletException {
		try {
			String startPage = req.getParameter(START_PAGE_PARAMETER);

			if (startPage != null) {
				int maxVisitedUris = Integer
						.parseInt(getDefaultedParameter(req, MAX_VISITED_URIS_PARAMETER, DEFAULT_MAX_VISITED_URIS));

				SortedSet<UriEntry> uriEntries = crawler.crawl(new URI(startPage), maxVisitedUris);

				setTemplateParameter(COLLECTED_LINKS_ATTRIBUTE,
						InfoService.sortBy(uriEntries, UriEntry.DISTANCE_COMPARATOR));
				setTemplateParameter(DOMAIN_STATISTICAL_INFO_ATTRIBUTE,
						InfoService.collectDomainStatisticalInfo(uriEntries));
				setTemplateParameter(START_PAGE_PARAMETER, startPage);
				setTemplateParameter(MAX_VISITED_URIS_PARAMETER, String.valueOf(maxVisitedUris));
			}

		} catch (URISyntaxException | NumberFormatException e) {
			throw new ServletException(e);
		}
	}

	private String getRedirectionResource() {
		return "info.html";
	}

	protected Map<String, Object> getTemplateParameters() {
		Map<String, Object> attributes = (Map<String, Object>) getServletContext()
				.getAttribute(TEMPLATE_PARAMETERS_ATTRIBUTE);
		if (attributes == null) {
			attributes = new HashMap<>();
			getServletContext().setAttribute(TEMPLATE_PARAMETERS_ATTRIBUTE, attributes);
		}
		return attributes;
	}

	protected Object getTemplateParameter(String name) {
		return getTemplateParameters().get(name);
	}

	protected void setTemplateParameter(String name, Object value) {
		getTemplateParameters().put(name, value);
	}

	protected String getParameter(HttpServletRequest req, String paramName, String exceptionMessage)
			throws ServletException {
		String parameter = req.getParameter(paramName);
		if (parameter == null) {
			throw new ServletException(exceptionMessage);
		}
		return parameter;
	}

	protected String getDefaultedParameter(HttpServletRequest req, String paramName, String defValue) {
		String parameter = req.getParameter(paramName);
		if (parameter == null) {
			return defValue;
		}
		return parameter;
	}

	protected Object getTemplateParameter(String attrName, String exceptionMessage) throws ServletException {
		Object parameter = getTemplateParameter(attrName);
		if (parameter == null) {
			throw new ServletException(exceptionMessage);
		}
		return parameter;
	}
}
