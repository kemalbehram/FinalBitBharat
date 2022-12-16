package com.mobiloitte.usermanagement.util;

import java.util.List;

public class Pagination<T> {
	private List<T> data;

	private Long size;

	public Pagination(List<T> list, Long size) {
		super();
		this.data = list;
		this.size = size;
	}

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}
}
