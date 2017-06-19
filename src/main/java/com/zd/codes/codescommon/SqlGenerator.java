package com.zd.codes.codescommon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SqlGenerator {
	private String table; //表名
	private Map<String, Object> objMap; //对象键值对
	private List<Object> objs; //所有对象值
	private List<String> names; //所有名称
	private List<String> fieldNames; // 类成员变量名称

	public SqlGenerator(String table) {
		super();
		this.table = table;
	}

	public Map<String, Object> getObjMap() {
		return objMap;
	}

	public void setObjMap(Map<String, Object> objMap) {
		this.objMap = objMap;
	}

	public List<Object> getObjs() {
		return objs;
	}

	public void setObjs(List<Object> objs) {
		this.objs = objs;
	}

	public List<String> getNames() {
		return names;
	}

	public void setNames(List<String> names) {
		this.names = names;
	}

	public List<String> getFieldNames() {
		return fieldNames;
	}

	public void setFieldNames(List<String> fieldNames) {
		this.fieldNames = fieldNames;
	}

	// -------业务------
	public String getInsertSql() {
		return getInsertSql(this.table);
	}

	public Object[] getInsertObject() {
		return objs.toArray();
	}

	public String getInsertSql(String table) {
		StringBuilder sql = new StringBuilder();
		StringBuilder par = new StringBuilder();
		for (String name : names) {
			sql.append(name + ",");
			par.append("?,");
		}
		return "insert into " + table + " (" + sql.toString().substring(0, sql.toString().length() - 1) + ") values("
				+ par.toString().substring(0, par.toString().length() - 1) + ")";
	}

	// 获取修改参数
	public Object[] getUpdateObject(String[] conditions) {
		if (null == conditions || conditions.length == 0) {
			return objs.toArray();
		}
		List<Object> insertObj = new ArrayList<Object>();
		List<String> conditionList = Arrays.asList(conditions);
		for (Map.Entry<String, Object> entry : objMap.entrySet()) {
			if (!conditionList.contains(entry.getKey())) {
				insertObj.add(entry.getValue());
			}
		}
		for (String s : conditionList) {
			insertObj.add(objMap.get(s));
		}
		return insertObj.toArray();
	}

	public String getUpdateSql(String[] conditions) {
		return getUpdateSql(this.table, conditions);
	}

	public String getUpdateSql(String table, String[] conditions) {
		if (null == conditions || conditions.length == 0 || names.size() == 0) {
			return "";
		}

		StringBuilder sql = new StringBuilder();
		List<String> conditionlist = Arrays.asList(conditions);
		boolean isGood = false;

		for (String name : names) {
			if (!conditionlist.contains(name)) {
				sql.append(" " + name + "=?,");
				isGood = true;
			}
		}
		StringBuilder condition = new StringBuilder();
		for (String s : conditions) {
			condition.append("and " + s + "=? ");
		}
		return isGood ? ("update " + table + " set" + sql.toString().substring(0, sql.toString().length() - 1) + " where 1=1 "
				+ condition.toString()) : "";
	}

	// 获取删除参数
	public Object[] getDeleteObject(String[] conditions) {
		if (null == conditions || conditions.length == 0) {
			return objs.toArray();
		}
		List<Object> objs = new ArrayList<Object>();
		for (String s : conditions) {
			objs.add(objMap.get(s));
		}
		return objs.toArray();
	}

	public String getDeleteSql(String[] conditions) {
		return this.getDeleteSql(this.table, conditions);
	}

	public String getDeleteSql(String table, String[] conditions) {
		if (null == conditions || conditions.length == 0 || names.size() == 0) {
			return "";
		}
		StringBuilder condition = new StringBuilder();
		for (String s : conditions) {
			condition.append("and " + s + "=? ");
		}
		return "delete from " + table + " where 1=1 " + condition.toString();
	}

}
