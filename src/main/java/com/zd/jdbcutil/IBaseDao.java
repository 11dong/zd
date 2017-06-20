package com.zd.jdbcutil;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author zd
 * 
 */
public interface IBaseDao<T extends Serializable> {
	// 创建数据
	public int insertObj(T entity);

	// 根据id修改
	public int updateByID(T entity);

	// 根据条件修改
	public int updateByCondition(T entity, String[] conditions);

	// 查询所有
	public List<T> findAll();

	// 根据id查询
	public T findByID(String id);

	// 查询条数
	public int getRows();

	// 根据map条件查询
	public int getRows(Map<String, Object> filterMap);

	// 根据自定义条件查询
	public int getRows(ConditionBuilder condition);

	// 根据参数条件删除
	public int deleteByCondition(String tableName, String condition, Object[] args);

	// 根据参数条件删除
	public int deleteByCondition(String condition, Object[] args);

	// 根据map分页查询
	public List<T> getByConduition(Map<String, Object> filterMap, PageParameters par);

	// 根据map查询
	public List<T> getByConduition(Map<String, Object> filterMap);

	// 根据map联表分页查询
	public List<T> joinQueryByConduition(JoinQuery jq, Map<String, Object> filterMap, PageParameters par);

	// 根据map联表查询
	public List<T> joinQueryByConduition(JoinQuery jq, Map<String, Object> filterMap);

	// 根据map联表分页查询条数
	public Integer joinQueryRows(JoinQuery jq, Map<String, Object> filterMap);

	// 根据自定义条件分页查询
	public List<T> getByConduition(ConditionBuilder condition, PageParameters par);

	// 根据自定义条件查询
	public List<T> getByConduition(ConditionBuilder condition);

	// 根据自定义条件联表分页查询
	public List<T> joinQueryByConduition(JoinQuery jq, ConditionBuilder condition, PageParameters par);

	// 根据自定义条件联表查询
	public List<T> joinQueryByConduition(JoinQuery jq, ConditionBuilder condition);

	// 根据自定义条件联表查询
	public Integer joinQueryRows(JoinQuery jq, ConditionBuilder condition);

	// 根据id删除
	public int deleteById(String id);

	// 获取JdbcTemplate
	public JdbcTemplate getJdbcTemplate();

}
