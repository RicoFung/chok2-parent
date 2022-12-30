package chok.devwork.pojo;

public class ChokDto<T> extends ChokDtoBase<T>
{
	private static final long serialVersionUID = 1L;
	
	public ChokDto()
	{
		super();
	}

	public ChokDto(T data)
	{
		super(data);
	}

	@Override
	public String toString()
	{
		return "ChokResultDTO [toString()=" + super.toString() + "]";
	}

}
