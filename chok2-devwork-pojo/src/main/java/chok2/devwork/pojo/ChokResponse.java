package chok2.devwork.pojo;

public class ChokResponse<T> extends ChokResponseBase<T>
{
	private static final long serialVersionUID = 1L;
	
	public ChokResponse()
	{
		super();
	}

	public ChokResponse(T data)
	{
		super(data);
	}

	@Override
	public String toString()
	{
		return "ChokResponse [toString()=" + super.toString() + "]";
	}

}
