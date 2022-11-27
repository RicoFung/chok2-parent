package chok.devwork.handler;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import chok.common.RestConstants;
import chok.devwork.pojo.ChokDtoBase;

public class CHandler<T, R extends ChokDtoBase<T>>
{
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	protected ObjectMapper objMapper = new ObjectMapper();
	
	public R execute(Object ro, BindingResult br, R dto, Callback<T, R> callback)
	{
		try
		{
			// 获取操作用户信息
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			// 创建时间
			Long time = System.currentTimeMillis();
			if (log.isDebugEnabled())
			{
				log.debug("==> ro：{}", objMapper.writeValueAsString(ro));
			}
			if (br != null && br.hasErrors())
			{
				dto.setSuccess(false);
				dto.setCode(RestConstants.ERROR_CODE1);
				dto.setMsg(getValidMsgs(br));
				return dto;
			}
			// executeProcess
			dto = callback.process(dto, auth, time);
			if (dto.isSuccess())
			{
				// executeSuccess
				callback.success(dto);
			}
			else
			{
				// executeError
				callback.error(dto);
				log.error(dto.getMsg());
			}
		}
		catch (Exception e)
		{
			log.error("<== Exception：{}", e);
			dto.setSuccess(false);
			dto.setCode(RestConstants.ERROR_CODE1);
			dto.setMsg(e.getMessage());
			// executeError
			callback.error(dto);
		}
		finally
		{
			if (log.isDebugEnabled())
			{
				try
				{
					log.debug("<== dto：{}", objMapper.writeValueAsString(dto));
				}
				catch (JsonProcessingException e)
				{
					log.error("<== JsonProcessingException: {}", e.getMessage());
				}
			}
		}
		return dto;
	}
	
	protected String getValidMsgs(BindingResult validResult)
	{
		StringBuilder validMsgsBuilder = new StringBuilder();
		List<String> validMsgList = getValidMsgList(validResult);
		for (String validMsg : validMsgList)
		{
			validMsgsBuilder.append(validMsg + ";");
		}
		return StringUtils.removeEnd(validMsgsBuilder.toString(), ";");
	}
	
	protected List<String> getValidMsgList(BindingResult validResult)
	{
		List<String> validMsgList = new ArrayList<String>();
		for (ObjectError oError : validResult.getAllErrors())
		{
			validMsgList.add(oError.getDefaultMessage());
		}
		return validMsgList;
	}
	
	public abstract static class Callback<T, R extends ChokDtoBase<T>>
	{
		/**
		 * 执行进行
		 * 
		 * @param dto
		 * @param auth
		 * @param time
		 * @return
		 * @throws Exception
		 */
		protected abstract R process(R dto, Authentication auth, Long time) throws Exception;

		/**
		 * 执行成功
		 * 
		 * @param dto
		 * @return
		 */
		protected R success(R dto)
		{
			return dto;
		}

		/**
		 * 执行失败
		 * 
		 * @param dto
		 * @return
		 */
		protected R error(R dto)
		{
			return dto;
		}

	}
}
