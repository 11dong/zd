package com.zd.codes.codescommon;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class ConditionBuilder {
	private String sql;
	private Object[] values;

	public String getSql() {
		return sql;
	}

	public Object[] getValues() {
		return values;
	}

	public static void main(String[] args) {
		ConditionBuilder co = new ConditionBuilder.Builder().in("123", new String[] { "123", "323" }).build();
		System.out.println(co.getSql());
	}

	@Override
	public String toString() {
		String str = "";
		for (Object obj : values) {
			str += obj + " ";
		}
		return "Condition [sql: " + sql + ", values=" + str + "]";
	}

	public ConditionBuilder() {

	}

	private ConditionBuilder(Builder b) {
		StringBuilder sb = new StringBuilder();
		List<Object> list = new ArrayList<Object>();
		for (Entry<String, Object[]> entry : b.getSqlMaps().entrySet()) {
			sb.append(" " + entry.getKey());
			if (entry.getValue() != null && entry.getValue().length != 0) {
				for (Object obj : entry.getValue()) {
					list.add(obj);
				}
			}
		}
		this.sql = sb.toString();
		this.values = list.toArray();
	}

	public static class Builder {
		private Map<String, Object[]> sqlMaps = new LinkedHashMap<String, Object[]>();

		private Map<String, Object[]> getSqlMaps() {
			return sqlMaps;
		}

		public Builder() {
		}

		private String logic = "and";

		private void isCondition(String condition) {
			if (condition == null || condition.equals("") || (condition.trim().indexOf(" ") != -1) || (condition.indexOf("=") != -1))
				throw new RuntimeException("非法字段");
		}

		public ConditionBuilder build() { // 构建，返回一个新对象
			return new ConditionBuilder(this);
		}

		public Builder isNull(String field) {
			isCondition(field);
			sqlMaps.put(field + " is null " + this.logic, null);
			return this;
		}

		public Builder isNull(LogicType type, String field) {
			isCondition(field);
			sqlMaps.put(type.name() + " " + field + " is null ", null);
			return this;
		}

		public Builder isNotNull(String field) {
			isCondition(field);
			sqlMaps.put(this.logic + " " + field + " is not null ", null);
			return this;
		}

		public Builder isNotNull(LogicType type, String field) {
			isCondition(field);
			sqlMaps.put(type.name() + " " + field + " is not null ", null);
			return this;
		}

		public Builder equalTo(String field, Object value) {
			isCondition(field);
			sqlMaps.put(this.logic + " " + field + " =? ", new Object[] { value });
			return this;
		}

		public Builder equalTo(LogicType type, String field, Object value) {
			isCondition(field);
			sqlMaps.put(type.name() + " " + field + " =? ", new Object[] { value });
			return this;
		}

		public Builder notEqualTo(String field, Object value) {
			isCondition(field);
			sqlMaps.put(this.logic + " " + field + " <>? ", new Object[] { value });
			return this;
		}

		public Builder notEqualTo(LogicType type, String field, Object value) {
			isCondition(field);
			sqlMaps.put(type.name() + " " + field + " <>? ", new Object[] { value });
			return this;
		}

		public Builder greaterThan(String field, Object value) {
			isCondition(field);
			sqlMaps.put(this.logic + " " + field + " >? ", new Object[] { value });
			return this;
		}

		public Builder greaterThan(LogicType type, String field, Object value) {
			isCondition(field);
			sqlMaps.put(type.name() + " " + field + " >? " + type.name(), new Object[] { value });
			return this;
		}

		public Builder greaterThanOrEqualTo(String field, Object value) {
			isCondition(field);
			sqlMaps.put(this.logic + " " + field + " >=? ", new Object[] { value });
			return this;
		}

		public Builder greaterThanOrEqualTo(LogicType type, String field, Object value) {
			isCondition(field);
			sqlMaps.put(type.name() + " " + field + " >=? ", new Object[] { value });
			return this;
		}

		public Builder lessThan(String field, Object value) {
			isCondition(field);
			sqlMaps.put(this.logic + " " + field + " <? ", new Object[] { value });
			return this;
		}

		public Builder lessThan(LogicType type, String field, Object value) {
			isCondition(field);
			sqlMaps.put(type.name() + " " + field + " <? ", new Object[] { value });
			return this;
		}

		public Builder lessThanOrEqualTo(String field, Object value) {
			isCondition(field);
			sqlMaps.put(this.logic + " " + field + " <=? ", new Object[] { value });
			return this;
		}

		public Builder lessThanOrEqualTo(LogicType type, String field, Object value) {
			isCondition(field);
			sqlMaps.put(type.name() + " " + field + " <=? ", new Object[] { value });
			return this;
		}

		public Builder in(String field, Object[] values) {
			isCondition(field);
			if (null == values) {
				return this;
			}
			String q = "";
			for (int i = 0; i < values.length; i++) {
				q += "?,";
			}
			if (q.length() > 0)
				q = q.substring(0, q.length() - 1);
			sqlMaps.put(this.logic + " " + field + " in(" + q + ") ", values);
			return this;
		}

		public Builder inStrList(String field, String[] values) {
			return in(field, values);
		}

		public Builder inIntList(String field, Integer[] values) {
			return in(field, values);
		}

		public Builder inString(String field, String values) {
			isCondition(field);
			sqlMaps.put(this.logic + " " + field + " in(" + values + ") ", null);
			return this;
		}

		public Builder in(LogicType type, String field, Object[] values) {
			isCondition(field);
			if (null == values) {
				return this;
			}
			String q = "";
			for (int i = 0; i < values.length; i++) {
				q += "?,";
			}
			if (q.length() > 0)
				q = q.substring(0, q.length() - 1);
			sqlMaps.put(type.name() + " " + field + " in(" + q + ") ", values);
			return this;
		}

		public Builder notIn(String field, Object[] values) {
			isCondition(field);
			if (null == values) {
				return this;
			}
			String q = "";
			for (int i = 0; i < values.length; i++) {
				q += "?,";
			}
			if (q.length() > 0)
				q = q.substring(0, q.length() - 1);
			sqlMaps.put(this.logic + " " + field + " not in(" + q + ") ", values);
			return this;
		}

		public Builder notInString(String field, String[] values) {
			return this.notIn(field, values);
		}

		public Builder notIn(LogicType type, String field, Object[] values) {
			isCondition(field);
			if (null == values) {
				return this;
			}
			String q = "";
			for (int i = 0; i < values.length; i++) {
				q += "?,";
			}
			if (q.length() > 0)
				q = q.substring(0, q.length() - 1);
			sqlMaps.put(type.name() + " " + field + " not in(" + q + ") ", values);
			return this;
		}

		public Builder between(String field, Object begin, Object end) {
			isCondition(field);
			sqlMaps.put(this.logic + " " + field + " between ? and ? ", new Object[] { begin, end });
			return this;
		}

		public Builder between(LogicType type, String field, Object begin, Object end) {
			isCondition(field);
			sqlMaps.put(type.name() + " " + field + " between ? and ? ", new Object[] { begin, end });
			return this;
		}

		public Builder notBetween(String field, Object begin, Object end) {
			isCondition(field);
			sqlMaps.put(this.logic + " " + field + " not between ? and ? ", new Object[] { begin, end });
			return this;
		}

		public Builder notBetween(LogicType type, String field, Object begin, Object end) {
			isCondition(field);
			sqlMaps.put(type.name() + " " + field + " not between ? and ? ", new Object[] { begin, end });
			return this;
		}

		public Builder like(String field, Object value) {
			isCondition(field);
			sqlMaps.put(this.logic + " " + field + " Like ? ", new Object[] { value });
			return this;
		}

		public Builder like(LogicType type, String field, Object value) {
			isCondition(field);
			sqlMaps.put(type.name() + " " + field + " Like ? ", new Object[] { value });
			return this;
		}

		public Builder notLike(String field, Object value) {
			isCondition(field);
			sqlMaps.put(this.logic + " " + field + " not Like ? ", new Object[] { value });
			return this;
		}

		public Builder notLike(LogicType type, String field, Object value) {
			isCondition(field);
			sqlMaps.put(type.name() + " " + field + " not Like ? ", new Object[] { value });
			return this;
		}

	}

}
