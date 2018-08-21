package com.roli.apsimock.common;

import org.springframework.web.servlet.view.freemarker.FreeMarkerView;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 自定义FreeMarkerView，用来定义项目的全局路径
 * @author Administrator
 *
 */
public class MyFreeMarkerView extends FreeMarkerView {
	
	private static final String CONTEXT_PATH = "base"; 
	
	@Override
	protected void exposeHelpers(Map<String, Object> model,HttpServletRequest request) throws Exception {
		model.put(CONTEXT_PATH, request.getContextPath());
		super.exposeHelpers(model, request);
	}

}