package chok.devwork;

import java.util.List;

import org.apache.ibatis.session.SqlSession;


public abstract class BaseDaoPlus
{
	/**
	 * 返回 SqlSession
	 * @return
	 */
	protected abstract SqlSession getSqlSession();
	/**
	 * 用于返回命名空间的全路径 Class.getName()
	 * @return Class
	 */
	protected abstract String getSqlNamespace();
	
	private String _statement = null;
	
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
	
	public int add(String statementName, Object entity)
	{
		return this.getSqlSession().insert(getStatementName(statementName), entity);
	}
	
	public int del(String statementName, String[] ids)
	{
		return this.getSqlSession().delete(getStatementName(statementName), ids);
	}
	
	public int upd(String statementName, Object entity)
	{
		return this.getSqlSession().update(getStatementName(statementName), entity);
	}
	
	public <T> T getOne(String statementName, Object query)
	{
		return this.getSqlSession().selectOne(getStatementName(statementName), query);
	}
	
	public <E> List<E> getList(String statementName, Object query)
	{
		return this.getSqlSession().selectList(getStatementName(statementName), query);
	}
	
	public int getCount(String statementName, Object query)
	{
		return this.getSqlSession().selectOne(getStatementName(statementName), query);
	}
	
	public int add(Object entity)
	{
		return this.getSqlSession().insert(getStatementName("add"), entity);
	}
	
	public int del(String[] ids)
	{
		return this.getSqlSession().delete(getStatementName("del"), ids);
	}
	
	public int upd(Object entity)
	{
		return this.getSqlSession().update(getStatementName("upd"), entity);
	}
	
	public <T> T getOne(Object query)
	{
		return this.getSqlSession().selectOne(getStatementName("getOne"), query);
	}
	
	public <E> List<E> getList(Object query)
	{
		return this.getSqlSession().selectList(getStatementName("getList"), query);
	}
	
	public int getCount(Object query)
	{
		return this.getSqlSession().selectOne(getStatementName("getCount"), query);
	}
	
	public int create(String statementName, Object entity)
	{
		return this.getSqlSession().insert(getStatementName(statementName), entity);
	}
	
	public int remove(String statementName, String[] ids)
	{
		return this.getSqlSession().delete(getStatementName(statementName), ids);
	}
	
	public int modify(String statementName, Object entity)
	{
		return this.getSqlSession().update(getStatementName(statementName), entity);
	}
	
	public int create(Object entity)
	{
		return this.getSqlSession().insert(getStatementName("create"), entity);
	}
	
	public int remove(String[] ids)
	{
		return this.getSqlSession().delete(getStatementName("remove"), ids);
	}
	
	public int modify(Object entity)
	{
		return this.getSqlSession().update(getStatementName("modify"), entity);
	}
	
}
