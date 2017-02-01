package com.rdfs.core.utils;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rdfs.core.bean.UserDto;
import com.rdfs.core.redis.JedisUtil;


public class AuthUtil {

	private static Logger log = LoggerFactory.getLogger(AuthUtil.class);

	/**
	 * 获取JUID
	 * 
	 * @param request
	 * @return
	 */
	public static String getJuid(HttpServletRequest request){
		String juid = request.getHeader("juid");
		if (RdfsUtils.isEmpty(juid)) {
			juid = request.getParameter("juid");
		}
		/*String render = request.getHeader("Referer");
		if(render.indexOf("?") != -1 && render.indexOf("juid")!=-1){
			String juid = render.substring(render.indexOf("juid")+5,render.length());
			return juid;
		}
		return null;*/
		return juid;
	}
	
	/**
	 * 获取UserModel对象
	 * 
	 * @return
	 */
	public static UserDto getUserDto(HttpServletRequest request){
		String juid = getJuid(request);
		UserDto userDto = getUserDto(juid);
		return userDto;
	}

	public static UserDto getUserDto(String juid) {
		UserDto UserDto = null;
		if (RdfsUtils.isNotEmpty(juid)) {
			String userJson = JedisUtil.getString(juid);
			UserDto = (UserDto) JsonUtil.fromJson(userJson, UserDto.class);
			if (RdfsUtils.isEmpty(UserDto)) {
				log.debug(RdfsUtils.merge("没获取到JUID为[{0}]的UserDto用户对象", juid));
			}
		}
		return UserDto;
	}
	
	public static boolean compareUserDto(HttpServletRequest request){
		//String render = request.getHeader("Referer");
		//if(!StringUtils.isBlank(render)&&render.indexOf("?") != -1 && render.indexOf("juid")!=-1){
			//String juid = render.substring(render.indexOf("juid")+5,render.length());
			UserDto userDto = getUserDto(request);
			if(userDto!=null){
				//if(userDto.getJuid().equals(juid)){
					String clientIp = getClientIpAddr(request);
					String clientKey = request.getHeader("USER-AGENT");
					if(!StringUtils.isBlank(userDto.getClientIp()) && !StringUtils.isBlank(clientIp) && userDto.getClientIp().equals(clientIp)){
						if(!StringUtils.isBlank(userDto.getClientKey()) && !StringUtils.isBlank(clientKey) && userDto.getClientKey().equals(clientKey)){
							return true;
						}
					}
				//}
			}
		//}
		return false;
	}
	
	/**
	 * 获取登录用户IP地址
	 * 
	 * @param request
	 * @return
	 */
	public static String getClientIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("X-real-ip");
		if (RdfsUtils.isEmpty(ip)) {
			ip = request.getHeader("X-Forward-For");
		}
		if (RdfsUtils.isEmpty(ip) || StringUtils.equalsIgnoreCase("unknown", ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (RdfsUtils.isEmpty(ip) || StringUtils.equalsIgnoreCase("unknown", ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (RdfsUtils.isEmpty(ip)) {
			ip = request.getRemoteAddr();
		}
		if (StringUtils.indexOf(ip, "0:0") != -1) {
			ip = "127.0.0.1";
		}
		return ip;
	}

}
