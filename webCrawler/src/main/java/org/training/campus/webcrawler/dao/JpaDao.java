package org.training.campus.webcrawler.dao;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Properties;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.training.campus.webcrawler.dao.model.Page;
import org.training.campus.webcrawler.dao.model.Research;
import org.training.campus.webcrawler.service.data.DomainData;
import org.training.campus.webcrawler.service.data.UriEntry;

public class JpaDao implements AutoCloseable {

	private static final String USER_KEY = "javax.persistence.jdbc.user";
	private static final String PASSWORD_KEY = "javax.persistence.jdbc.password";
	private static final String URL_KEY = "javax.persistence.jdbc.url";

	private static final String PERSISTENT_UNIT = "data";

	private EntityManager entityManager;
	private EntityManagerFactory entityManagerFactory;

	private final Properties props;

	private JpaDao() {
		props = new Properties();
	}

	private static class Holder {
		private static JpaDao dao = new JpaDao();
	}

	public static JpaDao getInstance() {
		return Holder.dao;
	}

	public static EntityManager getEntityManager() {
		return Objects.requireNonNull(Holder.dao.entityManager,
				"DAO instance should be initialized first before using instance of EntityManager");
	}

	public synchronized void init(String url, String user, String password) {
		if (entityManager == null) {
			props.put(URL_KEY, url);
			props.put(USER_KEY, user);
			props.put(PASSWORD_KEY, password);
			entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENT_UNIT, props);
			entityManager = entityManagerFactory.createEntityManager(props);
		}
	}

	@Override
	public void close() {
		if (entityManager != null) {
			try {
				entityManager.close();
				entityManager = null;
			} catch (IllegalStateException e) {
				e.printStackTrace();
			}
		}
		if (entityManagerFactory != null) {
			try {
				entityManagerFactory.close();
				entityManagerFactory = null;
			} catch (IllegalStateException e) {
				e.printStackTrace();
			}
		}
	}

	private Research persistDomainData(DomainData data) {
		Research research = Research.builder().time(LocalDateTime.now()).startPage(data.startPage())
				.maxUriFetched(data.maxVisitedUris()).fetchedLinkCount(data.linkCount()).maxDistance(data.maxDistance())
				.externalLinksCount(data.externalLinksCount()).elapsedTime(data.elapsedTime()).build();
		getEntityManager().persist(research);
		return research;
	}

	private void persistLink(Research research, UriEntry uriEntry) {
		Page page = Page.builder().research(research).uri(uriEntry.getUri().toString()).distance(uriEntry.getDistance())
				.parentUri(String.valueOf(uriEntry.getParentUri())).externalLinksCount(uriEntry.getExternalLinksCount())
				.build();
		getEntityManager().persist(page);
	}

	public void saveLinksDomainData(DomainData domainData) {
		EntityTransaction tx = getEntityManager().getTransaction();
		try {
			tx.begin();
			var research = persistDomainData(domainData);
			for (var link : domainData.uriEntries()) {
				persistLink(research, link);
			}
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			throw new DataAccessException(e);
		}
	}

}
