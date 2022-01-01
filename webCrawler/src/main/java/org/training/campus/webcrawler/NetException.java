package org.training.campus.webcrawler;

public class NetException extends RuntimeException {

	public NetException(Exception e) {
		super(e);
	}

	public NetException(String msg, Exception e) {
		super(msg, e);
	}

}
