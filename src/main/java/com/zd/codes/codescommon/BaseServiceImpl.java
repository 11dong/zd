package com.zd.codes.codescommon;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public abstract class BaseServiceImpl<T extends Serializable> implements IBaseService<T> {

	@Autowired
	private IBaseDao<T> basedao; // 泛型依赖注入

	public IBaseDao<T> getBaseDao() {
		return basedao;
	}

	// -----------------------------------------
	public int insert(T obj) {
		return basedao.insertObj(obj);
	}

	public int deleteById(String id) {
		return basedao.deleteById(id);
	}

	public int updateById(T obj) {
		return basedao.updateByID(obj);
	}

	public T getById(String id) {
		return basedao.findByID(id);
	}

	public Integer queryCount() {
		return basedao.getRows();
	}

	// -----------------------------------------
	public List<T> query(T obj) {
		return basedao.getByConduition(this.genMapCondition(obj));
	}

	public List<T> query(T obj, PageParameters par) {
		return basedao.getByConduition(this.genMapCondition(obj), par);
	}

	public Integer queryCount(T obj) {
		return basedao.getRows(this.genMapCondition(obj));
	}

	public List<T> query(ConditionBuilder condition) {
		return basedao.getByConduition(condition);
	}

	public List<T> query(ConditionBuilder condition, PageParameters par) {
		return basedao.getByConduition(condition, par);
	}

	public Integer queryCount(ConditionBuilder condition) {
		return basedao.getRows(condition);
	}

	public List<T> query(JoinQuery jq, ConditionBuilder condition) {
		return basedao.joinQueryByConduition(jq, condition);
	}

	public List<T> query(JoinQuery jq, ConditionBuilder condition, PageParameters par) {
		return basedao.joinQueryByConduition(jq, condition, par);
	}

	public Integer queryCount(JoinQuery jq, ConditionBuilder condition) {
		return basedao.joinQueryRows(jq, condition);
	}

	public List<T> query(JoinQuery jq, T obj) {
		return basedao.joinQueryByConduition(jq, this.genMapCondition(obj));
	}

	public List<T> query(JoinQuery jq, T obj, PageParameters par) {
		return basedao.joinQueryByConduition(jq, this.genMapCondition(obj), par);
	}

	public Integer queryCount(JoinQuery jq, T obj) {
		return basedao.joinQueryRows(jq, this.genMapCondition(obj));
	}

	// -----------------------------------------
	public DataAndTal queryListAndCount(T obj, PageParameters par) {
		return new DataAndTal(this.queryCount(obj), this.query(obj, par));
	}

	public DataAndTal queryListAndCount(ConditionBuilder condition, PageParameters par) {
		return new DataAndTal(this.queryCount(condition), this.query(condition, par));
	}

	public DataAndTal queryListAndCount(JoinQuery jq, ConditionBuilder condition, PageParameters par) {
		return new DataAndTal(this.queryCount(new JoinQuery(jq.getSelectAs(), " count(*) ", jq.getJionTable()), condition),
				this.query(jq, condition, par));
	}

	public DataAndTal queryListAndCount(JoinQuery jq, T obj, PageParameters par) {
		return new DataAndTal(this.queryCount(new JoinQuery(jq.getSelectAs(), " count(*) ", jq.getJionTable()), obj),
				this.query(jq, obj, par));
	}

}
