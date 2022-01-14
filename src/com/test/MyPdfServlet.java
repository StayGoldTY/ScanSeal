package com.test;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.kinggrid.pdfviewer.PdfViewer;

public class MyPdfServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		try {
			PdfViewer.execute(request, response);
		} catch (Throwable e) {
			// 记录日志
			e.printStackTrace();
			String contentType = request.getContentType();
			if("application/json".equals(contentType)){
				JSONObject obj = new JSONObject();
				obj.put("status", false);
				obj.put("message", "程序异常，请联系管理员！");
				response.getWriter().write(obj.toJSONString());
			}
		}
	}

}
