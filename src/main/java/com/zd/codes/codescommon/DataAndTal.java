package com.zd.codes.codescommon;

import java.util.List;

public class DataAndTal {
	private int tal = 0;
	private List<?> list;

	public DataAndTal() {

	}

	public DataAndTal(int tal, List<?> list) {
		super();
		this.tal = tal;
		this.list = list;
	}

	public int getTal() {
		return tal;
	}

	public void setTal(int tal) {
		this.tal = tal;
	}

	public List<?> getList() {
		return list;
	}

	public void setList(List<?> list) {
		this.list = list;
	}

}
