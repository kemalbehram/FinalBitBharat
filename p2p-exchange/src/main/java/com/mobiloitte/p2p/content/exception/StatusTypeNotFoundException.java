package com.mobiloitte.p2p.content.exception;

public class StatusTypeNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public StatusTypeNotFoundException() {
		super();
	}

	public StatusTypeNotFoundException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public StatusTypeNotFoundException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public StatusTypeNotFoundException(Throwable arg0,Throwable arg1,Throwable arg2) {
		super(arg0);
	}

	public StatusTypeNotFoundException(Throwable arg0) {
		super(arg0);
	}
}

