package org.training.campus.webcrawler.collector;

public class NetException extends RuntimeException {

	public NetException(Exception e) {
		super(e);
	}

	public NetException(String msg, Exception e) {
		super(msg, e);
	}

}
