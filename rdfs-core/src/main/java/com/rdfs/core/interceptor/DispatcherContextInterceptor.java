package com.rdfs.core.interceptor;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.rdfs.core.spring.SpringDispatcherContextHolder;

/**
 * 拦截没有登陆请求下访问方法
 * @author shaow
 *
 */
public class DispatcherContextInterceptor extends
		HandlerPreparInterceptor {
	
	/**/
	//出错返回页面
	private String redirectURL = "/error.jsp";
	
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) {
		
		try {
			 
			HttpSession session = request.getSession();
	        Object obj = session.getAttribute("loginUser");
			//String uri = request.getServletPath()+ (request.getPathInfo() == null ? "" : request.getPathInfo());
			
			if(obj ==null){
	    		response.sendError(405);
				response.sendRedirect(request.getContextPath() + redirectURL);
		    }
			SpringDispatcherContextHolder.initDispatcherContext(response);
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		SpringDispatcherContextHolder.resetDispatcherContext();
	}
}
