package chok2.devwork.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;


public abstract class BaseDao
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
	
	public <T> T getOne(String statementName, Object param)
	{
		return this.getSqlSession().selectOne(getStatementName(statementName), param);
	}
	
	public <E> List<E> getList(String statementName, Object param)
	{
		return this.getSqlSession().selectList(getStatementName(statementName), param);
	}
	
	public int getCount(String statementName, Object param)
	{
		return this.getSqlSession().selectOne(getStatementName(statementName), param);
	}
	
	public <T> T getOne(Object param)
	{
		return this.getSqlSession().selectOne(getStatementName("getOne"), param);
	}
	
	public <E> List<E> getList(Object param)
	{
		return this.getSqlSession().selectList(getStatementName("getList"), param);
	}
	
	public int getCount(Object param)
	{
		return this.getSqlSession().selectOne(getStatementName("getCount"), param);
	}
	
}
