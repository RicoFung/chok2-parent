package chok.devwork.springboot.pojo;

import chok.common.RestConstants;

public class ChokResult<T>
{
	private boolean	success		= true;
	private String	code		= RestConstants.SUCCESS_CODE;
	private String	msg			= "Success!";
	private String	path		= "";
	private String	timestamp	= "";
	private T		data;
	
	public boolean isSuccess()
	{
		return success;
	}
	public void setSuccess(boolean success)
	{
		this.success = success;
	}
	public String getCode()
	{
		return code;
	}
	public void setCode(String code)
	{
		this.code = code;
	}
	public String getMsg()
	{
		return msg;
	}
	public void setMsg(String msg)
	{
		this.msg = msg;
	}
	public String getPath()
	{
		return path;
	}
	public void setPath(String path)
	{
		this.path = path;
	}
	public String getTimestamp()
	{
		return timestamp;
	}
	public void setTimestamp(String timestamp)
	{
		this.timestamp = timestamp;
	}
	public T getData()
	{
		return data;
	}
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
