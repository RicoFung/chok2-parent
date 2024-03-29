package chok2.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 将long、int、float数组转化为Long、Integer、FLoat数组
 */
public class CollectionUtil
{
	public static Long[] toLongArray(long[] array)
	{
		Long[] arr = new Long[array.length];
		for (int i = 0; i < array.length; i++)
		{
			arr[i] = new Long(array[i]);
		}
		return arr;
	}

	public static Integer[] toIntegerArray(int[] array)
	{
		Integer[] arr = new Integer[array.length];
		for (int i = 0; i < array.length; i++)
		{
			arr[i] = new Integer(array[i]);
		}
		return arr;
	}

	public static Float[] toFloatArray(float[] array)
	{
		Float[] arr = new Float[array.length];
		for (int i = 0; i < array.length; i++)
		{
			arr[i] = new Float(array[i]);
		}
		return arr;
	}

	public static Long[] strToLongArray(String str, String separator)
	{
		String[] sArr = str.split(",");
		Long[] lArr = new Long[sArr.length];
		for (int i = 0; i < sArr.length; i++)
		{
			lArr[i] = Long.valueOf(sArr[i]);
		}
		return lArr;
	}

	public static List<Object> getKey(Map<?, ?> map, Object value)
	{
		List<Object> keyList = new ArrayList<>();
		for (Object key : map.keySet())
		{
			if (map.get(key).equals(value))
			{
				keyList.add(key);
			}
		}
		return keyList;
	}
}
