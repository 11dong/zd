package com.zd.jdbcutil;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zd.jdbcutil.DbColumn.ColumType;


/**
 * jdbc对象解析帮助工具
 * 
 * @author 11
 *
 */
public class JdbcHelper {

	private static final String svuId = "serialVersionUID";
	private String table; // 初始化后不允许修改
	private String key;// 初始化后不允许修改
	private ObjectMapper<?> rowMapper; // 数据绑定
	// private String queryColumnStr; //查询字段，暂时弃用
	// 获取rowMapper

	public ObjectMapper<?> getRowMapper() {
		return rowMapper;
	}

	@Override
	public String toString() {
		return "JdbcHelper [table=" + table + ", key=" + key + ", rowMapper=" + rowMapper + "]";
	}

	// 初始化rowMapper 表格信息
	public JdbcHelper(ObjectMapper<?> rowMapper) {
		this.rowMapper = rowMapper;
		initTableInfo(rowMapper.getObjClass());
	}

	// 仅是获取table与key信息，与数据无关
	public void initTableInfo(Class<? extends Object> clazz) {
		initTbale(clazz);
		Field fields[] = clazz.getDeclaredFields();
		// StringBuilder queryColumn = new StringBuilder();
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			String fieldName = field.getName();
			// 序列化字段不要
			if (svuId.equals(fieldName)) {
				continue;
			}
			String column = fieldName;
			if (field.isAnnotationPresent(DbColumn.class)) {
				DbColumn dbColumn = (DbColumn) field.getAnnotation(DbColumn.class);
				// 忽略
				if (dbColumn.type().equals(ColumType.IGNORE)) {
					continue;
				}
				// 只查询
				if (dbColumn.type().equals(ColumType.QUERY_ONLY)) {
					continue;
				}
				// 获取注解字段名称
				if (dbColumn.value() != null && !"".equals(dbColumn.value())) {
					column = dbColumn.value();
				}
				if (dbColumn.key()) {
					this.key = column;
				}
			}
			// queryColumn.append("," + column);
		}
		// queryColumnStr = queryColumn.substring(1, queryColumn.length());
	}

	@SuppressWarnings("unchecked")
	private void initTbale(@SuppressWarnings("rawtypes") Class clazz) {
		if (clazz.isAnnotationPresent(DbTable.class)) {
			DbTable dbTable = (DbTable) clazz.getAnnotation(DbTable.class);
			table = dbTable.value();
		}
	}

	public String getTable() {
		return this.table;
	}

	public String getKey() {
		return this.key;
	}

	// -------------------刷新数据键值对-------------------
	public SqlGenerator getSqlGenerator(Object obj) {
		SqlGenerator sg = new SqlGenerator(this.table);
		Map<String, Object> objMap = new HashMap<String, Object>();
		List<String> fieldNames = new ArrayList<String>();

		this.genMap(obj, null, objMap, fieldNames);

		sg.setObjMap(objMap);
		sg.setFieldNames(fieldNames);
		this.genObjAndNames(sg);
		return sg;
	}

	private void genMap(Object obj, List<String> widthOut, Map<String, Object> objMap, List<String> fieldNames) {
		if (null == obj) {
			return;
		}
		Field fields[] = obj.getClass().getDeclaredFields();
		objMap.clear();
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			try {
				String fieldName = field.getName();
				fieldNames.add(fieldName);
				if (svuId.equals(fieldName)) {
					continue;
				}
				String column = fieldName;
				// 获取注解
				if (field.isAnnotationPresent(DbColumn.class)) {
					DbColumn dbColumn = (DbColumn) field.getAnnotation(DbColumn.class);
					// 只查询
					if (dbColumn.type().equals(ColumType.QUERY_ONLY)) {
						continue;
					}
					// 忽略
					if (dbColumn.type().equals(ColumType.IGNORE)) {
						continue;
					}
					if (dbColumn.value() != null && !"".equals(dbColumn.value())) {
						column = dbColumn.value();
					}
				}

				if (null != widthOut && widthOut.contains(column)) {
					continue;
				}
				String firstLetter = fieldName.substring(0, 1).toUpperCase();
				String getter = "get" + firstLetter + fieldName.substring(1);
				Method method = obj.getClass().getMethod(getter, new Class[] {});
				Object value = method.invoke(obj, new Object[] {});
				if (value != null) {
					if (String.class.equals(field.getType())) {
						if ("".equals((String) value) || "null".equals((String) value)) {
							continue;
						}
					}
					objMap.put(column, value);
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}

	private void genObjAndNames(SqlGenerator sg) {
		if (null == sg.getObjMap()) {
			return;
		}
		List<Object> objs = new ArrayList<Object>();
		List<String> names = new ArrayList<String>();
		for (Map.Entry<String, Object> entry : sg.getObjMap().entrySet()) {
			objs.add(entry.getValue());
			names.add(entry.getKey());
		}
		sg.setObjs(objs);
		sg.setNames(names);
	}

	// --------------------------------------------业务----------------------------------------------

	public String getDeleteByIdSql() {
		return "delete from " + table + " where " + this.key + " =? ";
	}

	public String getFindAllSql() {
		return "select * from " + this.table;
	}

	public String getFindByIdSql() {
		return "select * from " + this.table + " where " + this.key + "=? ";
	}

	public String getGetRowsSql() {
		return "select count(1) from " + this.table + " where 1=1 ";
	}

	public String getQuerySql(String mycolumn) {
		return "select " + mycolumn + "  from " + this.table;
	}

	public String getQuerySql() {
		return "select * from " + this.table + " where 1=1 ";
	}

}
