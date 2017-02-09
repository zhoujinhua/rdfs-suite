package com.rdfs.hibernate.service.impl;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;
import org.hibernate.criterion.Order;

import com.rdfs.core.bean.Page;
import com.rdfs.hibernate.enums.OperMode;
import com.rdfs.hibernate.enums.OrderMode;
import com.rdfs.hibernate.hibernate.CrapCriteria;
import com.rdfs.hibernate.service.HibernateService;
import com.rdfs.hibernate.support.HibernateSupport;
import com.rdfs.hibernate.utils.BeanInvokeUtils;

public class HibernateServiceSupport extends HibernateSupport implements HibernateService{

	@Override
	@SuppressWarnings("unchecked")
	public <T extends Serializable> Page<T> pageList(T t, int pn, OrderMode orderMode, String orderParam,
			OperMode operMode, String... params) {
		if(t == null){
			throw new RuntimeException("查询失败,bean不能为空.");
		}
		CrapCriteria<T> cri = (CrapCriteria<T>) getSession().getCriteria(t.getClass());
		cri.addRestriction(t, operMode, params);
		if(orderMode != null){
			try{
				t.getClass().getDeclaredField(orderParam);
			} catch(Exception e){
				throw new RuntimeException("排序字段不存在.");
			}
			if(orderMode == OrderMode.ASC){
				cri.addOrder(Order.asc(orderParam));
			} else {
				cri.addOrder(Order.desc(orderParam));
			}
		}
		return cri.getResultList(pn);
	}
	
	@Override
	public <T extends Serializable> Page<T> pageList(T t, int pn, OperMode operMode, String...params) {
		return pageList(t, pn, null, null, operMode, params);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public <T extends Serializable> Page<T> pageList(int pn, String hql) {
		return getSession().createQuery(hql).getResultList(pn);
	}
	
	public List<?> pageSqlList(int pn, String sql){
		return (List<?>) getSession().createNativeQuery(sql).getResultList(pn);
	}
	
	@Override
	public <T extends Serializable> List<T> getList(Class<T> clazz, OperMode operMode, Map<String,Object> map) {
		return getSession().getCriteria(clazz).addRestriction(operMode, map).getResultList();
	}
	
	@Override
	public <T extends Serializable> List<T> getList(T t, String...params) {
		return getList(t, OperMode.EQ, params);
	}
	
	@Override
	public <T extends Serializable> List<T> getList(T t, OperMode operMode, String...params) {
		return getList(t, null, null, operMode, params);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public <T extends Serializable> List<T> getList(T t, OrderMode orderMode, String field, OperMode operMode, String...params) {
		if(t == null){
			throw new RuntimeException("查询失败,bean不能为空.");
		}
		CrapCriteria<T> crit = (CrapCriteria<T>) getSession().getCriteria(t.getClass()).addRestriction(t, operMode, params);
		if(orderMode != null && field != null && !"".equals(field)){
			try{
				t.getClass().getDeclaredField(field);
			} catch(Exception e){
				throw new RuntimeException("排序字段不存在.");
			}
			if(orderMode == OrderMode.ASC){
				crit.addOrder(Order.asc(field));
			} else {
				crit.addOrder(Order.desc(field));
			}
		}
		return crit.getResultList();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public <T extends Serializable> List<T> getList(String hql) {
		return getSession().createQuery(hql).getResultList();
	}

	@Override
	public List<?> getSqlList(String sql, Integer point, Integer step) {
		if(point!=null && step!=null){
			String build = sql + " limit " + point*step + "," + step;
			return getSession().createNativeQuery(build).getResultList();
		} 
		return getSession().createNativeQuery(sql).getResultList();
	}
	

	public List<?> getSqlList(String sql){
		return getSqlList(sql, null, null);
	}
	
	@Override
	public <T extends Serializable> T saveEntity(T t) {
		getSession().persist(t);
		return t;
	}

	@Override
	public <T extends Serializable> T updateEntity(T t, String... params) {
		getSession().update(t, params);
		return t;
	}

	@Override
	public <T extends Serializable> void deleteEntity(T t) {
		getSession().remove(t);
	}

	@Override
	public <T extends Serializable> T getEntityById(Class<T> type, int id, boolean init) {
		T t = (T) getSession().find(type, id);
		if(init && t!=null){
			Field[] fields = type.getDeclaredFields();
			for(Field field : fields){
				if(field.getType().getSimpleName().equals("List")){
					try {
						Hibernate.initialize(BeanInvokeUtils.invokeMethod(t, field.getName()));
					} catch (Exception e) {
						throw new RuntimeException("方法调用失败");
					}
				}
			}
		}
		return t;
	}
	
	@Override
	public <T extends Serializable> T getEntityByCode(Class<T> type, String id, boolean init) {
		T t = (T) getSession().find(type, id);
		if(init && t!=null){
			Field[] fields = type.getDeclaredFields();
			for(Field field : fields){
				if(field.getType().getSimpleName().equals("List")){
					try {
						Hibernate.initialize(BeanInvokeUtils.invokeMethod(t, field.getName()));
					} catch (Exception e) {
						throw new RuntimeException("方法调用失败");
					}
				}
			}
		}
		return t;
	}

}
