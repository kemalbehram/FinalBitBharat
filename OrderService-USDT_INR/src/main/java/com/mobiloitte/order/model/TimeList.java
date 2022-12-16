package com.mobiloitte.order.model;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class TimeList<T> {
	private List<TimeWrapper<T>> list;

	public TimeList(List<TimeWrapper<T>> list) {
		super();
		this.setList(new LinkedList<>(list));
	}

	public List<T> getList() {
		list = list.parallelStream().filter(TimeWrapper::isNotExpired).collect(Collectors.toList());
		return list.parallelStream().map(t -> t.getData().get()).collect(Collectors.toList());
	}

	public void setList(List<TimeWrapper<T>> list) {
		this.list = list;
	}

	public void addItem(TimeWrapper<T> item) {
		this.list.add(item);
	}
}
