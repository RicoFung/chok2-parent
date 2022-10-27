package chok.devwork;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import com.google.common.collect.Lists;


public abstract class BaseDao<T,PK>
{
	/**
	 * 用于返回命名空间的全路径Class.getName()
	 * @return Class
	 */
	protected abstract Class<T> getEntityClass();
	protected abstract SqlSession getSqlSession();
	private String _statement = null;
	private static int DEFAULT_OFFSET = 1;
	private static int DEFAULT_LIMIT = 5;
	
	/**
	 * 返回命名空间的值
	 * @return String
	 */
	private String getSqlNamespace()
	{
		return getEntityClass().getName();
	}

	/**
	 * 获取需要操作sql的id，当getEntityClass().getName()无法满足时，可以重载此方法
	 * @param statementName SQL的ID(不包含namespace)
	 * @return String
	 */
	protected String getStatementName(String statementName)
	{
		if(_statement == null)
		{
			_statement = getSqlNamespace() + ".";
		}
		return _statement + statementName;
	}
	
	public int add(T po)
	{
		return this.getSqlSession().insert(getStatementName("add"), po);
	}

	public int addBatch(List<T> list)
	{
		return this.add("addBatch", list);
	}
	
	public int addBatch(List<T> list, int size)
	{
		// Lambda 修改外部变量时，外部变量必须定义为数组
		final int[] r = {0};
		List<List<T>> parts = Lists.partition(list, size);
		parts.stream().forEach(pList ->
		{
			r[0] += this.addBatch(pList);
		});
		return r[0];
	}
	
	public int upd(T po)
	{
		return this.getSqlSession().update(getStatementName("upd"), po);
	}
	
	public int updBatch(List<T> list)
	{
		return this.upd("updBatch", list);
	}
	
	public int updBatch(List<T> list, int size)
	{
		// Lambda 修改外部变量时，外部变量必须定义为数组
		final int[] r = {0};
		List<List<T>> parts = Lists.partition(list, size);
		parts.stream().forEach(pList ->
		{
			r[0] += this.updBatch(pList);
		});
		return r[0];
	}

	public int del(PK id)
	{
		return this.getSqlSession().delete(getStatementName("del"), id);
	}

	@SuppressWarnings("unchecked")
	public T get(PK id)
	{
		return (T) this.getSqlSession().selectOne(getStatementName("get"), id);
	}
	
	public Map<String, Object> getMap(Map<String, Object> m)
	{
		return getMap("getMap", m);
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> getMap(String statementName, Map<String, Object> m)
	{
		return (Map<String, Object>) this.getSqlSession().selectOne(getStatementName(statementName), m);
	}
	
	public Object getDynamic(Map<String, Object> param)
	{
		return this.get("getDynamic", param);
	}
	
//	public T getOnSelectFields(String[] selectFields, String pkKey, PK pkValue)
//	{
//		return getOnSelectFields("getOnSelectFields", "selectFields", selectFields, pkKey, pkValue);
//	}
//	
//	@SuppressWarnings("unchecked")
//	public T getOnSelectFields(String statementName, String selectFieldsKey, String[] selectFieldsValue, String pkKey, PK pkValue)
//	{
//		Map<String, Object> param = new HashMap<String, Object>();
//		param.put(selectFieldsKey, selectFieldsValue);
//		param.put(pkKey, pkValue);
//		return (T) this.getSqlSession().selectOne(getStatementName(statementName), param);
//	}
//	
//	public Map<String, Object> getMapOnSelectFields(String[] selectFields, String pkKey, PK pkValue)
//	{
//		return getMapOnSelectFields("getMapOnSelectFields", "selectFields", selectFields, pkKey, pkValue);
//	}
//	
//	@SuppressWarnings("unchecked")
//	public Map<String, Object> getMapOnSelectFields(String statementName, String selectFieldsKey, String[] selectFieldsValue, String pkKey, PK pkValue)
//	{
//		Map<String, Object> param = new HashMap<String, Object>();
//		param.put(selectFieldsKey, selectFieldsValue);
//		param.put(pkKey, pkValue);
//		return (Map<String, Object>) this.getSqlSession().selectOne(getStatementName(statementName), param);
//	}
	
	public List<T> query(Map<String, Object> m)
	{
		return this.getSqlSession().selectList(getStatementName("query"), m);
	}

	public List<T> queryDynamic(Map<String, Object> param)
	{
		return this.query("queryDynamic", param);
	}
	
//	public List<T> queryOnSelectFields(Map<String, Object> param)
//	{
//		return query("queryOnSelectFields", param);
//	}
//	
//	public List<T> queryOnSelectFields(String[] selectFields, Map<String, Object> param)
//	{
//		return queryOnSelectFields("queryOnSelectFields", "selectFields", selectFields, param);
//	}
//	
//	@SuppressWarnings("unchecked")
//	public List<T> queryOnSelectFields(String statementName, String selectFieldsKey, String[] selectFieldsValue, Map<String, Object> param)
//	{
//		param.put(selectFieldsKey, selectFieldsValue);
//		return (List<T>) this.getSqlSession().selectList(getStatementName(statementName), param);
//	}
	
	public List<Object> queryMap(Map<String, Object> m)
	{
		return this.getSqlSession().selectList(getStatementName("queryMap"), m);
	}
	
	public List<Map<String, Object>> queryMap(String statementName, Map<String, Object> m)
	{
		return this.getSqlSession().selectList(getStatementName(statementName), m);
	}
	
//	public List<Map<String, Object>> queryMapOnSelectFields(Map<String, Object> param)
//	{
//		return queryMap("queryMapOnSelectFields", param);
//	}
//	
//	public List<Map<String, Object>> queryMapOnSelectFields(String[] selectFields, Map<String, Object> param)
//	{
//		return queryMapOnSelectFields("queryMapOnSelectFields", "selectFields", selectFields, param);
//	}
//	
//	public List<Map<String, Object>> queryMapOnSelectFields(String statementName, String selectFieldsKey, String[] selectFieldsValue, Map<String, Object> param)
//	{
//		param.put(selectFieldsKey, selectFieldsValue);
//		return this.getSqlSession().selectList(getStatementName(statementName), param);
//	}
	
	public int getCount(Map<String, Object> m)
	{
		return (Integer) this.getSqlSession().selectOne(getStatementName("getCount"), m);
	}

	public List queryMapPage(Map<String, Object> m)
	{
		List result = queryMap(m);
		return result;
	}
	
	public int add(String statementName, Object param)
	{
		return this.getSqlSession().insert(getStatementName(statementName), param);
	}
	
	public int del(String statementName, Object param)
	{
		return this.getSqlSession().delete(getStatementName(statementName), param);
	}
	
	public int upd(String statementName, Object param)
	{
		return this.getSqlSession().update(getStatementName(statementName), param);
	}
	
	public Object get(String statementName, Object param)
	{
		return this.getSqlSession().selectOne(getStatementName(statementName), param);
	}
	
	public List<T> query(String statementName, Object param)
	{
		return this.getSqlSession().selectList(getStatementName(statementName), param);
	}
	
	public int getCount(String statementName, Object param)
	{
		return this.getSqlSession().selectOne(getStatementName(statementName), param);
	}
}
