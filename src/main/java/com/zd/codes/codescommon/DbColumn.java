package com.zd.codes.codescommon;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DbColumn {
	// 字段与表格的关系
	public enum ColumType {
		ALL, QUERY_ONLY, IGNORE // 可查可写，直查不写，不查不写
	};

	String value() default ""; // 名称

	ColumType type() default ColumType.ALL; // 类型

	boolean key() default false; // 是否主键
}