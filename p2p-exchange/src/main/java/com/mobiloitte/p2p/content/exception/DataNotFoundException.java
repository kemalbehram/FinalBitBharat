package com.mobiloitte.p2p.content.exception;

public class DataNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public DataNotFoundException() {
		super("There is No any data on database");

	}

}