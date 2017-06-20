package com.zd.jdbcutil;

/**
 * 分页参数
 * 
 * @author 11
 *
 */
public class PageParameters {

	private int page = 1;
	private int rows = 10;
	private String sort;
	private String order;
	private String group;

	public PageParameters(int rows) {
		this.rows = rows;
	}

	public PageParameters() {
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

}
