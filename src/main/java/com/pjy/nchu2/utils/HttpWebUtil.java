package com.pjy.nchu2.utils;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * HttpWebUtils工具类
 * 
 * @author Style
 * @date 2019/7/4
 */
public class HttpWebUtil {
	
	/**
	 * 	获取当前request	
	 * @return HttpServletRequest
	 */
	public static HttpServletRequest getRequest() {
		HttpServletRequest request =
			    ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		return request;
	}
	
	/**
	 * 	获取当前response
	 * @return HttpServletResponse
	 */
	public static HttpServletResponse getResponse() {
		HttpServletResponse response =
			    ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
		return response;
	}
 
}
