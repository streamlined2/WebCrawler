package org.training.campus.webcrawler.dao;

public class DataAccessException extends RuntimeException {

	public DataAccessException(String msg) {
		super(msg);
	}

	public DataAccessException(Exception e) {
		super(e);
	}

}
