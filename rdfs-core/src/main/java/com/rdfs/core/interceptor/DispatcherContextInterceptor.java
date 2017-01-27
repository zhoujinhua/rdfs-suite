package com.rdfs.core.interceptor;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.rdfs.core.spring.SpringDispatcherContextHolder;
import com.rdfs.core.utils.AuthUtil;

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
			String uri = request.getServletPath()+ (request.getPathInfo() == null ? "" : request.getPathInfo());
			/*String render = request.getHeader("Referer");
			HttpSession session = request.getSession();
	        Object obj = session.getAttribute("loginUser");
			
			if(obj ==null){
	    		response.sendError(405);
				response.sendRedirect(request.getContextPath() + redirectURL);
		    }*/
			boolean flag = AuthUtil.compareUserDto(request);
	        if(!flag){
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
