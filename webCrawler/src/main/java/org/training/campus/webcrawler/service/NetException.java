package org.training.campus.webcrawler.service;

public class NetException extends RuntimeException {

	public NetException(Exception e) {
		super(e);
	}

	public NetException(String msg, Exception e) {
		super(msg, e);
	}

}
