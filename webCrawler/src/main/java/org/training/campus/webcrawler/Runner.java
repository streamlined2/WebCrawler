package org.training.campus.webcrawler;

import java.util.Objects;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.training.campus.webcrawler.controller.FCServlet;

public class Runner {

	private static final String CONTEXT = "/crawler";
	private static final String START_PAGE_PARAMETER = "startPage";
	private static final String MAX_VISITED_URIS_PARAMETER = "maxVisitedUris";
	private static final String JDBC_URL_PARAMETER = "dbUrl";
	private static final String JDBC_USER_PARAMETER = "dbUser";
	private static final String JDBC_PASSWORD_PARAMETER = "dbPassword";
	private static final String SERVER_PORT_PARAMETER = "dbPort";

	public static void main(String[] args) {

		var server = new Server(Integer.parseInt(System.getProperty(SERVER_PORT_PARAMETER)));

		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);

		context.setContextPath(CONTEXT);
		context.setInitParameter(FCServlet.START_PAGE_PARAMETER, System.getProperty(START_PAGE_PARAMETER));
		context.setInitParameter(FCServlet.MAX_VISITED_URIS_PARAMETER, System.getProperty(MAX_VISITED_URIS_PARAMETER));
		context.setInitParameter(FCServlet.JDBC_URL_PARAMETER, System.getProperty(JDBC_URL_PARAMETER));
		context.setInitParameter(FCServlet.JDBC_USER_PARAMETER,
				Objects.requireNonNullElse(System.getProperty(JDBC_USER_PARAMETER), ""));
		context.setInitParameter(FCServlet.JDBC_PASSWORD_PARAMETER,
				Objects.requireNonNullElse(System.getProperty(JDBC_PASSWORD_PARAMETER), ""));

		context.addServlet(FCServlet.class, "/");

		server.setHandler(context);

		try {
			server.start();
			server.join();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
