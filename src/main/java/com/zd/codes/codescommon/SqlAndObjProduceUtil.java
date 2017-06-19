package com.zd.codes.codescommon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


public class SqlAndObjProduceUtil {
	/**
	 * 根据参数拼装SQL
	 * 
	 * @param filterMap
	 * @return
	 */
	public static String genSql(Map<String, Object> filterMap) {
		if (filterMap != null && filterMap.size() > 0) {
			StringBuilder sb = new StringBuilder();
			for (Entry<String, Object> entry : filterMap.entrySet()) {
				Object val = entry.getValue();
				sb.append(" and ").append(entry.getKey());
				if (val != null && val.getClass().getName().equals("java.lang.String") && ((String) val).indexOf("%") != -1) {
					sb.append(" like ?");
				} else {
					sb.append("=?");
				}
			}
			return sb.toString();
		}
		return "";
	}

	/**
	 * 拼装SQL对应的参数
	 * 
	 * @param filterMap
	 * @param par
	 * @return
	 */
	public static Object[] genSqlParameters(Map<String, Object> filterMap, PageParameters par) {
		List<Object> p = new ArrayList<Object>();
		if (filterMap != null && filterMap.size() > 0) {
			for (Object obj : filterMap.values()) {
				p.add(obj);
			}
		}
		if (par != null) {
			p.add(par.getRows() * par.getPage() - par.getRows());
			p.add(par.getRows());

		}
		return p.toArray();
	}

	/**
	 * 拼装SQL对应的参数
	 * 
	 * @param filterMap
	 * @param par
	 * @return
	 */
	public static Object[] genSqlParameters(ConditionBuilder condition, PageParameters par) {
		List<Object> p = new ArrayList<Object>();
		if (null != condition) {
			p.addAll(Arrays.asList(condition.getValues()));
		}
		if (par != null) {
			p.add(par.getRows() * par.getPage() - par.getRows());
			p.add(par.getRows());
		}
		return p.toArray();
	}
}
