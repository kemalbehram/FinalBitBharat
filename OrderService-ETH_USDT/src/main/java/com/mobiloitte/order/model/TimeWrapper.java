package com.mobiloitte.order.model;

import java.util.Optional;

public class TimeWrapper<T> {
	private final T data;
	private long expirationTime;

	public Optional<T> getData() {
		if (data != null && isNotExpired())
			return Optional.of(data);
		return Optional.empty();
	}

	public TimeWrapper(T data, long expirationTime) {
		super();
		this.data = data;
		this.expirationTime = expirationTime;
	}

	public boolean isExpired() {
		return expirationTime < System.currentTimeMillis();
	}

	public boolean isNotExpired() {
		return expirationTime >= System.currentTimeMillis();
	}
}
