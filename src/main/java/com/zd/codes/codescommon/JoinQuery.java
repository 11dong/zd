package com.zd.codes.codescommon;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 
 * @author zd
 *
 */
public class JoinQuery {

	private String selectAs; // 主表定义
	private String queryData; // 查询参数
	private Map<String, String> jionTable; // 联查表组 table,on

	public JoinQuery(String selectAs, String queryData, Map<String, String> jionTable) {
		super();
		this.selectAs = selectAs;
		this.queryData = queryData;
		this.jionTable = jionTable;
	}

	public String getSelectAs() {
		return selectAs;
	}

	public void setSelectAs(String selectAs) {
		this.selectAs = selectAs;
	}

	public String getQueryData() {
		return queryData;
	}

	public void setQueryData(String queryData) {
		this.queryData = queryData;
	}

	public Map<String, String> getJionTable() {
		return jionTable;
	}

	public static void main(String[] args) {
		JoinQuery jq = new JoinQuery.Builder().setQueryData("a.*,u.nickname,g.goName").selectMainTableAs("a")
				.leftJoin("t_user u", "u.id = a.usid ").leftJoin("t_goods g", "g.id = a.goid ").build();
		System.out.println(jq.getSql("t_a"));
	}

	public String getSql(String table) {
		StringBuilder sb = new StringBuilder();
		sb.append("select");
		if (null != queryData && "" != queryData) {
			sb.append(" " + queryData + " ");
		}
		sb.append("from ").append(table);
		if (null != selectAs && "" != selectAs) {
			sb.append(" " + selectAs + " ");
		}
		for (Entry<String, String> entry : jionTable.entrySet()) {
			sb.append(" left join ").append(entry.getKey()).append(" on ").append(entry.getValue());
		}
		sb.append(" where 1=1 ");
		return sb.toString();
	}

	public void setJionTable(Map<String, String> jionTable) {
		this.jionTable = jionTable;
	}

	public JoinQuery(Builder b) {
		this.jionTable = b.getJionTable();
		this.queryData = b.getQueryData();
		this.selectAs = b.getSelectAs();
	}

	public static class Builder {

		private String selectAs; // 主表定义
		private String queryData; // 查询参数
		private Map<String, String> jionTable; // 联查表组 table,on

		public Builder() {
			jionTable = new LinkedHashMap<String, String>();
		}

		private String getSelectAs() {
			return selectAs;
		}

		private String getQueryData() {
			return queryData;
		}

		private Map<String, String> getJionTable() {
			return jionTable;
		}

		public JoinQuery build() { // 构建，返回一个新对象
			return new JoinQuery(this);
		}

		public Builder selectMainTableAs(String a) {
			this.selectAs = a;
			return this;
		}

		public Builder setQueryData(String queryData) {
			this.queryData = queryData;
			return this;
		}

		public Builder leftJoin(String table, String on) {
			this.jionTable.put(table, on);
			return this;
		}

	}
}
