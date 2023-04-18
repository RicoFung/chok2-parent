package chok2.devwork;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.ModelAttribute;

import chok2.util.JasperUtil;
import chok2.util.POIUtil;
import chok2.util.TimeUtil;

public class BaseRestController
{
	protected HttpServletRequest request;
	protected HttpServletResponse response;

	@ModelAttribute
	public void BaseInitialization(HttpServletRequest request, HttpServletResponse response)
	{
		this.request = request;
		this.response = response;
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
				
				response.reset();// 清空输出流
				response.setHeader("Content-Disposition", "attachment; filename="
						+ java.net.URLEncoder.encode(fileName, "UTF-8")
						+ "_"
						+ TimeUtil.formatDate(new Date(), "yyyyMMdd_HHmmss") + "." +"xlsx");
				if(exportType.equals("xlsx"))
					response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8");// 定义输出类型:xlsx
				else
					response.setContentType("application/msexcel;charset=UTF-8");// 定义输出类型:xls
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
	 * 导出_包含单个Tabel（不含基础控件）
	 * @param jasperFileName 模板文件名
	 * @param rptFileName 生成文件名
	 * @param rptFileFormat 生成文件格式(包括："pdf"/"xlsx"/"html")
	 * @param rptBizDatasetTableK Table控件业务数据集KEY
	 * @param rptBizDatasetTableV Table控件业务数据集VAL
	 * @param rptBizDatasetTableClazz Table控件数据类型 (三种可选：Object.class/Map.class/HashMap.class)
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
	 * 导出_包含多个Tabel控件（含基础控件）
	 * @param rptTemplateName 模板文件名
	 * @param rptFileName 生成文件名
	 * @param rptFileFormat 生成文件格式(包括："pdf"/"xlsx"/"html")
	 * @param rptBizDatasetKV 基础控件业务数据集（KEY-VALUE）
	 * @param rptBizDatasetTableKV Table控件业务数据集（KEY-VALUE）
	 * @param rptBizDatasetTableClazzes Table控件数据类型 (三种可选：Object.class/Map.class/HashMap.class)
	 * @throws Exception
	 */
	public void exportRptMultiTable(String rptTemplateName, String rptFileName, String rptFileFormat, Map<String, ?> rptBizDatasetKV, LinkedHashMap<String, List<?>> rptBizDatasetTableKV, Class<?>... rptBizDatasetTableClazzes) throws Exception 
	{
		JasperUtil.export(response, rptTemplateName, rptFileName, rptFileFormat, rptBizDatasetKV, rptBizDatasetTableKV, rptBizDatasetTableClazzes);
	}
	
}
