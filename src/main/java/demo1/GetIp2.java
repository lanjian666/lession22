package demo1;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class GetIp2
 */
public class GetIp2 extends HttpServlet {
	@Override
	protected long getLastModified(HttpServletRequest req) {
		return new Date().getTime()-30000;
	}
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String ip=getIPAddress(request);
		request.setAttribute("ip", ip);
		Map<String,String> map=new HashMap<String, String>();
		List<String> list =new ArrayList<String>();
		Enumeration enu=request.getHeaderNames();//取得全部头信息
	     while(enu.hasMoreElements()){            //以此取出头信息
	         String headerName=(String)enu.nextElement();
	        String headerValue=request.getHeader(headerName);//取出头信息内容
	        map.put(headerName, headerValue);
	     }
		response.setHeader("Content-type", "text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		
		Cookie cookie = new Cookie("cookiename","cookievalue");
		//设置Cookie最大生存时间,以秒为单位,负数的话为浏览器进程,关闭浏览器Cookie消失 
		cookie.setMaxAge(86400);//设置cookie过期时间为1个小时
		//设置路径，这个路径即该工程下都可以访问该cookie 如果不设置路径，那么只有设置该cookie路径及其子路径可以访问
		cookie.setPath("/");
		// 将Cookie添加到Response中,使之生效 
		response.addCookie(cookie);
		
		//Last-Modified:页面的最后生成时间 
	     response.setDateHeader("Last-Modified",new Date().getTime());
	    //Expires:过时期限值 
	     response.setDateHeader("Expires", System.currentTimeMillis() + 30000);
	     // 获取所有的cookie值
	    Cookie[] cookies = request.getCookies();  
	    Cookie cookie1 = null;
	    if (cookies!=null) {
	    	for (int i = 0; i < cookies.length; i++) {
	    		cookie1=cookies[i];
	    		list.add(cookie1.getName()+","+cookie1.getValue());
	    	}
		}
	    request.setAttribute("map", map);
	    request.setAttribute("list", list);
		request.getRequestDispatcher("index.jsp").forward(request,response);
	}
	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doGet(request, response);
	}
	public static String getIPAddress(HttpServletRequest request) {
	    String ip = null;    //X-Forwarded-For：Squid 服务代理
	    String ipAddresses = request.getHeader("X-Forwarded-For");
	    if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {        //Proxy-Client-IP：apache 服务代理
	        ipAddresses = request.getHeader("Proxy-Client-IP");
	    }
	    if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {        //WL-Proxy-Client-IP：weblogic 服务代理
	        ipAddresses = request.getHeader("WL-Proxy-Client-IP");
	    }
	    if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {        //HTTP_CLIENT_IP：有些代理服务器
	        ipAddresses = request.getHeader("HTTP_CLIENT_IP");
	    }
	    if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {        //X-Real-IP：nginx服务代理
	        ipAddresses = request.getHeader("X-Real-IP");
	    }    //有些网络通过多层代理，那么获取到的ip就会有多个，一般都是通过逗号（,）分割开来，并且第一个ip为客户端的真实IP
	    if (ipAddresses != null && ipAddresses.length() != 0) {
	        ip = ipAddresses.split(",")[0];
	    }    //还是不能获取到，最后再通过request.getRemoteAddr();获取
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
	        ip = request.getRemoteAddr();
	    }
	    return ip;
	}

}
