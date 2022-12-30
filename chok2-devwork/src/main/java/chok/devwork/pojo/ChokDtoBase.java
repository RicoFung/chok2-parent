package chok.devwork.pojo;

import chok.common.RestConstants;

public abstract class ChokDtoBase<T> implements ChokDtoInterface<T>
{
	private static final long serialVersionUID = 1L;
	
	private boolean	success		= true;
	private String	code		= RestConstants.SUCCESS_CODE;
	private String	msg			= "";
	private String	path		= "";
	private String	timestamp	= "";
	private T		data;
	
	public ChokDtoBase()
	{
		super();
	}

	public ChokDtoBase(T data)
	{
		super();
		this.data = data;
	}

	public boolean isSuccess()
	{
		return success;
	}

	public String getCode()
	{
		return code;
	}

	public String getMsg()
	{
		return msg;
	}

	public String getPath()
	{
		return path;
	}

	public String getTimestamp()
	{
		return timestamp;
	}

	public T getData()
	{
		return data;
	}
	
	@Override
	public void setSuccess(boolean success)
	{
		this.success = success;
	}

	@Override
	public void setCode(String code)
	{
		this.code = code;
	}
	
	@Override
	public void setMsg(String msg)
	{
		this.msg = msg;
	}

	@Override
	public void setPath(String path)
	{
		this.path = path;
	}

	@Override
	public void setTimestamp(String timestamp)
	{
		this.timestamp = timestamp;
	}

	@Override
	public void setData(T data)
	{
		this.data = data;
	}
	
	@Override
	public String toString()
	{
		return "ChokResultObject [success=" + success + ", code=" + code + ", msg=" + msg + ", path=" + path
				+ ", timestamp=" + timestamp + ", data=" + data + "]";
	}
	
}
