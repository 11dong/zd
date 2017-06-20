package com.zd.jdbcutil;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import com.zd.jdbcutil.DbColumn.ColumType;


//--------------jdbc自动装配，数据量过大不建议使用---------------
public class ObjectMapper<T> implements RowMapper<T> {
	private Class<?> className;
	private List<String> widthOut; // 不能包括的字段

	public ObjectMapper(Class<?> className, String[] widthOut) {
		this.className = className;
		this.widthOut = Arrays.asList(widthOut);
	}

	public Class<?> getObjClass() {
		return this.className;
	}

	public ObjectMapper(Class<?> className) {
		this.className = className;
	}

	@SuppressWarnings("unchecked")
	public T mapRow(ResultSet rs, int rowNum) throws SQLException {
		Object nt;
		try {
			nt = className.newInstance();
		} catch (InstantiationException e1) {
			e1.printStackTrace();
			return null;
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
			return null;
		}
		try {
			Field[] fields = className.getDeclaredFields();
			nt = className.newInstance();
			for (Field field : fields) {
				String fieldName = field.getName();
				String column = fieldName;
				// 获取注解
				if (field.isAnnotationPresent(DbColumn.class)) {
					DbColumn dbColumn = (DbColumn) field.getAnnotation(DbColumn.class);
					if (dbColumn.type().equals(ColumType.IGNORE)) {
						continue;
					}
					if (dbColumn.value() != null && !"".equals(dbColumn.value())) {
						column = dbColumn.value();
					}
				}
				// 跳过指定字段
				if (null != widthOut && widthOut.contains(column)) {
					continue;
				}
				// 如果结果中没有改field项则跳过
				try {
					rs.findColumn(column);
				} catch (Exception e) {
					continue;
				}
				// 修改相应filed的权限
				boolean accessFlag = field.isAccessible();
				field.setAccessible(true);
				String value = rs.getString(column);
				if (value != null) {
					setFieldValue(nt, field, value);
				}
				// 恢复相应field的权限
				field.setAccessible(accessFlag);
			}
			return ((T) nt);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	/** */
	/*
	 * 根据类型对具体对象属性赋值
	 */
	public void setFieldValue(Object form, Field field, String value) {

		String elemType = field.getType().toString();

		if (elemType.indexOf("boolean") != -1 || elemType.indexOf("Boolean") != -1) {
			try {
				field.set(form, Boolean.valueOf(value));
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		} else if (elemType.indexOf("byte") != -1 || elemType.indexOf("Byte") != -1) {
			try {
				field.set(form, Byte.valueOf(value));
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		} else if (elemType.indexOf("char") != -1 || elemType.indexOf("Character") != -1) {
			try {
				field.set(form, Character.valueOf(value.charAt(0)));
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		} else if (elemType.indexOf("double") != -1 || elemType.indexOf("Double") != -1) {
			try {
				field.set(form, Double.valueOf(value));
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		} else if (elemType.indexOf("float") != -1 || elemType.indexOf("Float") != -1) {
			try {
				field.set(form, Float.valueOf(value));
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		} else if (elemType.indexOf("int") != -1 || elemType.indexOf("Integer") != -1) {
			try {
				field.set(form, Integer.valueOf(value));
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		} else if (elemType.indexOf("long") != -1 || elemType.indexOf("Long") != -1) {
			try {
				field.set(form, Long.valueOf(value));
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		} else if (elemType.indexOf("short") != -1 || elemType.indexOf("Short") != -1) {
			try {
				field.set(form, Short.valueOf(value));
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		} else if (elemType.indexOf("BigDecimal") != -1) {
			try {
				field.set(form, new BigDecimal(value));
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		} else if (elemType.indexOf("Date") != -1) {
			try {
				if (isDate(value, "yyyy-MM-dd HH:mm:ss")) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					field.set(form, sdf.parse(value));
				} else if (isDate(value, "yyyy-MM-dd")) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					field.set(form, sdf.parse(value));
				}
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			}
		} else {
			try {
				field.set(form, (Object) value);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean isDate(String date, String format) {
		/**
		 * 判断日期格式和范围
		 */
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			sdf.parse(date);
			return true;
		} catch (ParseException e) {
			return false;
		}
	}
}