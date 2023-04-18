package chok2.devwork.pojo;

import java.io.Serializable;

public interface ChokDtoInterface<T> extends Serializable
{
	boolean isSuccess();

	void setSuccess(boolean success);

	String getCode();

	void setCode(String code);

	String getMsg();

	void setMsg(String msg);

	String getPath();

	void setPath(String path);

	String getTimestamp();

	void setTimestamp(String timestamp);

	T getData();

	void setData(T data);
}