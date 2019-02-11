package com.android.yawei.jhoa.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据集
 * 
 * @author sn
 * 
 * @param <T>
 */
public class DataSet<T> {

	private int totalCount = 0; // 数据总数，区别于list的大小。

	private List<T> list = new ArrayList<T>(); // 数据存储空间

	public DataSet() {
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public List<T> getList() {
		return list;
	}

	public void add(T t) {
		list.add(t);
	}

	public void addToHeader(T t) {
		list.add(0, t);
	}

	public T get(int location) {
		return list.get(location);
	}

	public void addToHeader(DataSet<T> dataSet) {
		totalCount = dataSet.totalCount;

		dataSet.list.addAll(list);
		List<T> temp = list;
		list = dataSet.list;
		dataSet.list = temp;
	}

	public void append(DataSet<T> dataSet) {
		totalCount = dataSet.totalCount;
		list.addAll(dataSet.list);
	}

	public void clear() {
		totalCount = 0;
		list.clear();
	}

	public boolean contains(T t) {
		return list.contains(t);
	}



	public T remove(int location) {
		return list.remove(location);
	}

	public boolean isEmpty() {
		return list.isEmpty();
	}

	public int size() {
		return list.size();
	}
}
