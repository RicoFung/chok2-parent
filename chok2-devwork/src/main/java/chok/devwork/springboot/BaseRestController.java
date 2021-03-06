package chok.devwork.springboot;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import chok.common.RestConstants;
import chok.common.RestResult;
import chok.util.JasperUtil;
import chok.util.POIUtil;
import chok.util.TimeUtil;

public class BaseRestController<T>
{
    @Autowired
    private Validator validator;
    
	protected RestResult restResult;

	protected ObjectMapper restMapper;
	
	protected HttpServletRequest request;
	protected HttpServletResponse response;

	@ModelAttribute
	public void BaseInitialization(HttpServletRequest request, HttpServletResponse response)
	{
		this.restResult = new RestResult();
		this.restMapper = new ObjectMapper();
		this.request = request;
		this.response = response;
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
	
	protected List<String> getValidMsgListBySet(Set<ConstraintViolation<?>> validSet)
	{
		List<String> validMsgList = new ArrayList<String>();
		for (ConstraintViolation<?> constraintViolation : (Set<ConstraintViolation<?>>)validSet) 
		{
			validMsgList.add(constraintViolation.getMessage());
		}
		return validMsgList;
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
	
	@SuppressWarnings("unchecked")
	protected String getValidMsgsBySet(Object validSet)
	{
		StringBuilder validMsgsBuilder = new StringBuilder();
		List<String> validMsgList = getValidMsgListBySet((Set<ConstraintViolation<?>>) validSet);
		for (String validMsg : validMsgList)
		{
			validMsgsBuilder.append(validMsg + ";");
		}
		return StringUtils.removeEnd(validMsgsBuilder.toString(), ";");
	}
	
	protected RestResult validImportBefore(MultipartFile file)
	{
		restResult = new RestResult();
		// ??????file
		if (file == null)
		{
			restResult.setSuccess(false);
			restResult.setCode(RestConstants.ERROR_CODE1);
			restResult.setMsg("file???????????????");
		}
		return restResult;
	}
	
	protected RestResult validImportBefore(MultipartFile file, String json, Class<?> clazz) throws JsonParseException, JsonMappingException, IOException
	{
		restResult = new RestResult();
		// ??????file
		if (file == null)
		{
			restResult.setSuccess(false);
			restResult.setCode(RestConstants.ERROR_CODE1);
			restResult.setMsg("file???????????????");
		}
		// ??????json
		if (json != null)
		{
			Object jsonDTO = restMapper.readValue(json, clazz);
			Set<ConstraintViolation<Object>> validSet = validator.validate(jsonDTO);
			if (validSet.size() > 0) 
			{
				restResult.setSuccess(false);
				restResult.setCode(RestConstants.ERROR_CODE1);
				restResult.setMsg(getValidMsgsBySet(validSet));
			}
		}
		return restResult;
	}
	
	public void export(List<?> list, String fileName, String title, String headerNames, String dataColumns, String exportType)
	{
		ByteArrayOutputStream ba = null;
		ServletOutputStream out = null;
		try
		{
			try 
			{
				ba = new ByteArrayOutputStream();
				ba = (ByteArrayOutputStream) POIUtil.writeExcel(ba, 
						fileName, 
						title, 
						headerNames, 
						dataColumns, 
						list);
				
				response.reset();// ???????????????
				response.setHeader("Content-Disposition", "attachment; filename="
						+ java.net.URLEncoder.encode(fileName, "UTF-8")
						+ "_"
						+ TimeUtil.formatDate(new Date(), "yyyyMMdd_HHmmss") + "." +"xlsx");
				if(exportType.equals("xlsx"))
					response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8");// ??????????????????:xlsx
				else
					response.setContentType("application/msexcel;charset=UTF-8");// ??????????????????:xls
				response.setContentLength(ba.size());
				out = response.getOutputStream();
				ba.writeTo(out);
				out.flush();
			}
			finally 
			{
				if (out != null) out.close();
				if (ba != null) ba.close();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * ??????_????????????Tabel????????????????????????
	 * @param jasperFileName ???????????????
	 * @param rptFileName ???????????????
	 * @param rptFileFormat ??????????????????(?????????"pdf"/"xlsx"/"html")
	 * @param rptBizDatasetTableK Table?????????????????????KEY
	 * @param rptBizDatasetTableV Table?????????????????????VAL
	 * @param rptBizDatasetTableClazz Table?????????????????? (???????????????Object.class/Map.class/HashMap.class)
	 * @throws Exception
	 */
	public void exportRptOneTable(String jasperFileName, String rptFileName, String rptFileFormat, String rptBizDatasetTableK, List<?> rptBizDatasetTableV, Class<?> rptBizDatasetTableClazz) throws Exception 
	{
		LinkedHashMap<String, List<?>> rptBizDatasetTableKV = new LinkedHashMap<String, List<?>>()
		{
			private static final long serialVersionUID = 1L;
			{
				put(rptBizDatasetTableK, rptBizDatasetTableV);
			}
		};
		exportRptMultiTable(jasperFileName, rptFileName, rptFileFormat, null, rptBizDatasetTableKV, rptBizDatasetTableClazz);
	}
	
	/**
	 * ??????_????????????Tabel???????????????????????????
	 * @param rptTemplateName ???????????????
	 * @param rptFileName ???????????????
	 * @param rptFileFormat ??????????????????(?????????"pdf"/"xlsx"/"html")
	 * @param rptBizDatasetKV ??????????????????????????????KEY-VALUE???
	 * @param rptBizDatasetTableKV Table????????????????????????KEY-VALUE???
	 * @param rptBizDatasetTableClazzes Table?????????????????? (???????????????Object.class/Map.class/HashMap.class)
	 * @throws Exception
	 */
	public void exportRptMultiTable(String rptTemplateName, String rptFileName, String rptFileFormat, Map<String, ?> rptBizDatasetKV, LinkedHashMap<String, List<?>> rptBizDatasetTableKV, Class<?>... rptBizDatasetTableClazzes) throws Exception 
	{
		JasperUtil.export(response, rptTemplateName, rptFileName, rptFileFormat, rptBizDatasetKV, rptBizDatasetTableKV, rptBizDatasetTableClazzes);
	}
	
}
