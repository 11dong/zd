package com.zd.codes.codescommon;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.StringUtils;

/**
 * @author zd
 * 
 */
public abstract class BaseDaoImpl<T extends Serializable> implements IBaseDao<T> {

	private static final Logger logger = LoggerFactory.getLogger(BaseDaoImpl.class);
	private JdbcHelper jdbcHelper;
	protected Class<T> tClass;

	protected JdbcHelper getJdbcHelper() {
		return jdbcHelper;
	}

	// 泛型类作为父类，可以获取子类的所有实际参数的类型
	private Class<?> getModelClass(Class<?> modelClass, int index) {
		Type genType = this.getClass().getGenericSuperclass();// 得到泛型父类
		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

		if (params.length - 1 < index) {
			modelClass = null;
			logger.error("--->未找到泛型类");
		} else {
			modelClass = (Class<?>) params[index];
		}
		return modelClass;
	}

	public BaseDaoImpl() {
		@SuppressWarnings("unchecked")
		Class<?> clazz = ((Class<T>) getModelClass(tClass, 0)); // 本类泛型
		if (clazz != null) {
			jdbcHelper = new JdbcHelper(new ObjectMapper<T>(clazz));
			logger.debug("==> table:" + jdbcHelper.getTable() + " key:" + jdbcHelper.getKey());
		}
	}

	// --------------------------------------------------------可公开接口---------------------------------------------------

	// ****************************删除******************
	
	public int deleteById(String id) {
		String sql = jdbcHelper.getDeleteByIdSql();
		Object[] obj = new Object[] { id };
		this.debug(sql, obj);
		return this.getJdbcTemplate().update(sql, obj);
	}

	
	public int deleteByCondition(String condition, Object[] args) {
		String sql = new StringBuilder().append("delete from ").append(jdbcHelper.getTable()).append(" where ").append(condition)
				.toString();
		this.debug(sql, args);
		return this.getJdbcTemplate().update(sql, args);
	}

	
	public int deleteByCondition(String tableName, String condition, Object[] args) {
		String sql = new StringBuilder().append("delete from ").append(tableName).append(" where ").append(condition).toString();
		this.debug(sql, args);
		return this.getJdbcTemplate().update(sql, args);
	}

	// *****************************************************添加**************************************************************************
	
	public int insertObj(T entity) {
		return this.insert(entity);
	}

	// ****************************************************修改**************************************************************************
	
	public int updateByID(T entity) {
		return this.updateByConditions(entity, new String[] { jdbcHelper.getKey() });
	}

	
	public int updateByCondition(T entity, String[] conditions) {
		return this.updateByConditions(entity, conditions);
	}

	// ***********************************************************根据页面条件查询记录条数***********************************************
	
	public int getRows() {
		String sql = jdbcHelper.getGetRowsSql();
		this.debug(sql, null);
		return this.getJdbcTemplate().queryForObject(sql, Integer.class);
	}

	
	public int getRows(Map<String, Object> filterMap) {
		return this.queryCount(jdbcHelper.getGetRowsSql(), filterMap);
	}

	
	public int getRows(ConditionBuilder condition) {
		return this.queryCount(jdbcHelper.getGetRowsSql(), condition);
	}

	
	public Integer joinQueryRows(JoinQuery jq, ConditionBuilder condition) {
		return this.queryCount(jq.getSql(jdbcHelper.getTable()), condition);
	}

	
	public Integer joinQueryRows(JoinQuery jq, Map<String, Object> filterMap) {
		return this.queryCount(jq.getSql(jdbcHelper.getTable()), filterMap);
	}

	// *****************************************************查询***************************************************************

	@SuppressWarnings("unchecked")
	
	public List<T> findAll() {
		String sql = jdbcHelper.getFindAllSql();
		this.debug(sql, null);
		return (List<T>) this.getJdbcTemplate().query(sql, jdbcHelper.getRowMapper());
	}

	@SuppressWarnings("unchecked")
	
	public T findByID(String id) {
		String sql = jdbcHelper.getFindByIdSql();
		Object[] obj = new Object[] { id };
		this.debug(sql, obj);
		List<T> list = (List<T>) this.getJdbcTemplate().query(sql, obj, jdbcHelper.getRowMapper());
		return list != null && list.size() == 1 ? list.get(0) : null;
	}

	@SuppressWarnings("unchecked")
	
	public List<T> getByConduition(Map<String, Object> filterMap, PageParameters par) {
		return this.queryList(jdbcHelper.getQuerySql(), filterMap, par, (RowMapper<T>) jdbcHelper.getRowMapper());
	}

	@SuppressWarnings("unchecked")
	
	public List<T> getByConduition(Map<String, Object> filterMap) {
		return this.queryList(jdbcHelper.getQuerySql(), filterMap, (RowMapper<T>) jdbcHelper.getRowMapper());
	}

	@SuppressWarnings("unchecked")
	
	public List<T> joinQueryByConduition(JoinQuery jq, Map<String, Object> filterMap, PageParameters par) {
		return this.queryList(jq.getSql(jdbcHelper.getTable()), filterMap, par, (RowMapper<T>) jdbcHelper.getRowMapper());
	}

	@SuppressWarnings("unchecked")
	
	public List<T> joinQueryByConduition(JoinQuery jq, Map<String, Object> filterMap) {
		return this.queryList(jq.getSql(jdbcHelper.getTable()), filterMap, (RowMapper<T>) jdbcHelper.getRowMapper());
	}

	@SuppressWarnings("unchecked")
	
	public List<T> getByConduition(ConditionBuilder condition, PageParameters par) {
		return this.queryList(jdbcHelper.getQuerySql(), condition, par, (RowMapper<T>) jdbcHelper.getRowMapper());
	}

	@SuppressWarnings("unchecked")
	
	public List<T> getByConduition(ConditionBuilder condition) {
		return this.queryList(jdbcHelper.getQuerySql(), condition, (RowMapper<T>) jdbcHelper.getRowMapper());
	}

	@SuppressWarnings("unchecked")
	
	public List<T> joinQueryByConduition(JoinQuery jq, ConditionBuilder condition, PageParameters par) {
		return this.queryList(jq.getSql(jdbcHelper.getTable()), condition, par, (RowMapper<T>) jdbcHelper.getRowMapper());
	}

	@SuppressWarnings("unchecked")
	
	public List<T> joinQueryByConduition(JoinQuery jq, ConditionBuilder condition) {
		return this.queryList(jq.getSql(jdbcHelper.getTable()), condition, (RowMapper<T>) jdbcHelper.getRowMapper());
	}

	// ----------------------------------------------------------半公开接口-----------------------------------------------------------------------

	protected Integer queryCount(String sql, Map<String, Object> filterMap) {
		Object[] obj = SqlAndObjProduceUtil.genSqlParameters(filterMap, null);
		String querysql = new StringBuilder().append(sql).append(SqlAndObjProduceUtil.genSql(filterMap)).toString();
		this.debug(querysql, obj);
		return this.getJdbcTemplate().queryForObject(querysql, obj, Integer.class);
	}

	protected Integer queryCount(String sql, ConditionBuilder condition) {
		Object[] obj = condition.getValues();
		this.debug(sql + condition.getSql(), obj);
		return this.getJdbcTemplate().queryForObject(sql + condition.getSql(), obj, Integer.class);
	}

	/**
	 * 返回具体的条数信息
	 * 
	 * @param sql
	 * @param filterMap
	 * @param par
	 * @param rowMapper
	 * @return
	 */
	protected List<T> queryList(String sql, Map<String, Object> filterMap, PageParameters par, RowMapper<T> rowMapper) {
		StringBuilder sb = new StringBuilder();
		if (!StringUtils.isEmpty(par.getGroup())) {
			sb.append(" group by ").append(par.getGroup());
		}
		if (!StringUtils.isEmpty(par.getSort())) {
			sb.append(" order by ").append(par.getSort());
		}
		if (!StringUtils.isEmpty(par.getOrder())) {
			if (!par.getOrder().equalsIgnoreCase("asc")) {
				sb.append(" desc");
			}
		}
		String querysql = new StringBuilder().append(sql).append(SqlAndObjProduceUtil.genSql(filterMap)).append(sb.toString())
				.append(" LIMIT ?,?  ").toString();
		Object[] obj = SqlAndObjProduceUtil.genSqlParameters(filterMap, par);
		this.debug(querysql, obj);
		return this.getJdbcTemplate().query(querysql, obj, rowMapper);
	}

	/**
	 * 返回待条件的所有信息
	 * 
	 * @param sql
	 * @param filterMap
	 * @param par
	 * @param rowMapper
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected List<T> queryList(String sql, Map<String, Object> filterMap, PageParameters par) {
		StringBuilder sb = new StringBuilder();
		if (!StringUtils.isEmpty(par.getGroup())) {
			sb.append(" group by ").append(par.getGroup());
		}
		if (!StringUtils.isEmpty(par.getSort())) {
			sb.append(" order by ").append(par.getSort());
		}
		if (!StringUtils.isEmpty(par.getOrder())) {
			if (!par.getOrder().equalsIgnoreCase("asc")) {
				sb.append(" desc");
			}
		}
		String querysql = new StringBuilder().append(sql).append(SqlAndObjProduceUtil.genSql(filterMap)).append(sb.toString())
				.append(SqlWord.LIMIT)
				.append(" ?,?  ").toString();
		Object[] obj = SqlAndObjProduceUtil.genSqlParameters(filterMap, par);
		this.debug(querysql, obj);
		return (List<T>) this.getJdbcTemplate().query(querysql, obj, jdbcHelper.getRowMapper());
	}

	protected List<T> queryList(String sql, Map<String, Object> filterMap, RowMapper<T> rowMapper) {
		String querysql = new StringBuilder().append(sql).append(SqlAndObjProduceUtil.genSql(filterMap)).toString();
		Object[] obj = SqlAndObjProduceUtil.genSqlParameters(filterMap, null);
		this.debug(querysql, obj);
		return this.getJdbcTemplate().query(querysql, obj, rowMapper);
	}

	@SuppressWarnings("unchecked")
	protected List<T> queryList(String sql, Map<String, Object> filterMap) {
		String querysql = new StringBuilder().append(sql).append(SqlAndObjProduceUtil.genSql(filterMap)).toString();
		Object[] obj = SqlAndObjProduceUtil.genSqlParameters(filterMap, null);
		this.debug(querysql, obj);
		return (List<T>) this.getJdbcTemplate().query(querysql, obj, jdbcHelper.getRowMapper());
	}

	/**
	 * 返回待条件的所有信息
	 * 
	 * @param sql
	 * @param filterMap
	 * @param par
	 * @param rowMapper
	 * @return
	 */
	protected List<T> queryList(String sql, Object[] obj, RowMapper<T> rowMapper) {
		String querysql = sql;
		this.debug(querysql, obj);
		return this.getJdbcTemplate().query(querysql, obj, rowMapper);
	}

	@SuppressWarnings("unchecked")
	protected List<T> queryList(String sql, Object[] obj) {
		String querysql = sql;
		this.debug(querysql, obj);
		return (List<T>) this.getJdbcTemplate().query(querysql, obj, jdbcHelper.getRowMapper());
	}

	/**
	 * 返回具体的条数信息
	 * 
	 * @param sql
	 * @param filterMap
	 * @param par
	 * @param rowMapper
	 * @return
	 */
	protected List<T> queryList(String sql, ConditionBuilder condition, PageParameters par, RowMapper<T> rowMapper) {
		StringBuilder sb = new StringBuilder();
		if (!StringUtils.isEmpty(par.getGroup())) {
			sb.append(" group by ").append(par.getGroup());
		}
		if (!StringUtils.isEmpty(par.getSort())) {
			sb.append(" order by ").append(par.getSort());
		}
		if (!StringUtils.isEmpty(par.getOrder())) {
			if (!par.getOrder().equalsIgnoreCase("asc")) {
				sb.append(" desc");
			}
		}
		String querysql = sql + (condition == null ? "" : condition.getSql()) + sb.toString() + " LIMIT ?,?  ";
		Object[] obj = SqlAndObjProduceUtil.genSqlParameters(condition, par);
		this.debug(querysql, obj);
		return this.getJdbcTemplate().query(querysql, obj, rowMapper);
	}

	/**
	 * 返回待条件的所有信息
	 * 
	 * @param sql
	 * @param filterMap
	 * @param par
	 * @param rowMapper
	 * @return
	 */
	protected List<T> queryList(String sql, ConditionBuilder condition, RowMapper<T> rowMapper) {
		String querysql = sql + condition.getSql();
		Object[] obj = condition.getValues();
		this.debug(querysql, obj);
		return this.getJdbcTemplate().query(querysql, obj, rowMapper);
	}

	@SuppressWarnings("unchecked")
	protected List<T> queryList(String sql, ConditionBuilder condition) {
		String querysql = sql + condition.getSql();
		Object[] obj = condition.getValues();
		this.debug(querysql, obj);
		return (List<T>) this.getJdbcTemplate().query(querysql, obj, jdbcHelper.getRowMapper());
	}

	/**
	 * 修改
	 * 
	 * @param table
	 * @param conditions
	 * @param obj
	 * @return
	 */
	protected int updateByConditions(T entity, String[] conditions) {
		SqlGenerator sg = jdbcHelper.getSqlGenerator(entity);
		String sql = sg.getUpdateSql(conditions);
		Object[] conditionsobjs = sg.getUpdateObject(conditions);
		this.debug(sql, conditionsobjs);
		return this.getJdbcTemplate().update(sql, conditionsobjs);
	}

	/**
	 * 添加
	 * 
	 * @param table
	 * @param obj
	 * @return
	 */
	protected int insert(T obj) {
		SqlGenerator sg = jdbcHelper.getSqlGenerator(obj);
		String sql = sg.getInsertSql();
		Object[] insertobjs = sg.getInsertObject();
		this.debug(sql, insertobjs);
		return this.getJdbcTemplate().update(sql, insertobjs);
	}

	// 打印
	private void debug(String sql, Object[] obj) {
		logger.debug("==> [jdbcTemplate] sql:  " + sql);
		if (obj != null) {
			logger.debug("==> [jdbcTemplate] Parameters:  " + Arrays.asList(obj).toString());
		}
	}
}
