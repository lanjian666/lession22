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
		return System.currentTimeMillis()-30000;
	}
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String ip=getIPAddress(request);
		request.setAttribute("ip", ip);
		Map<String,String> map=new HashMap<String, String>();
		List<String> list =new ArrayList<String>();
		Enumeration enu=request.getHeaderNames();//ȡ��ȫ��ͷ��Ϣ
	     while(enu.hasMoreElements()){            //�Դ�ȡ��ͷ��Ϣ
	         String headerName=(String)enu.nextElement();
	        String headerValue=request.getHeader(headerName);//ȡ��ͷ��Ϣ����
	        map.put(headerName, headerValue);
	     }
		response.setHeader("Content-type", "text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		
		Cookie cookie = new Cookie("cookiename","cookievalue");
		//����Cookie�������ʱ��,����Ϊ��λ,�����Ļ�Ϊ���������,�ر������Cookie��ʧ 
		cookie.setMaxAge(86400);//����cookie����ʱ��Ϊ1��Сʱ
		//����·�������·�����ù����¶����Է��ʸ�cookie ���������·������ôֻ�����ø�cookie·��������·�����Է���
		cookie.setPath("/");
		// ��Cookie��ӵ�Response��,ʹ֮��Ч 
		response.addCookie(cookie);
		
		//Last-Modified:ҳ����������ʱ�� 
	     response.setDateHeader("Last-Modified",new Date().getTime());
	    //Expires:��ʱ����ֵ 
	     response.setDateHeader("Expires", System.currentTimeMillis() + 30000);
	     // ��ȡ���е�cookieֵ
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
	    String ip = null;    //X-Forwarded-For��Squid �������
	    String ipAddresses = request.getHeader("X-Forwarded-For");
	    if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {        //Proxy-Client-IP��apache �������
	        ipAddresses = request.getHeader("Proxy-Client-IP");
	    }
	    if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {        //WL-Proxy-Client-IP��weblogic �������
	        ipAddresses = request.getHeader("WL-Proxy-Client-IP");
	    }
	    if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {        //HTTP_CLIENT_IP����Щ���������
	        ipAddresses = request.getHeader("HTTP_CLIENT_IP");
	    }
	    if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {        //X-Real-IP��nginx�������
	        ipAddresses = request.getHeader("X-Real-IP");
	    }    //��Щ����ͨ����������ô��ȡ����ip�ͻ��ж����һ�㶼��ͨ�����ţ�,���ָ�������ҵ�һ��ipΪ�ͻ��˵���ʵIP
	    if (ipAddresses != null && ipAddresses.length() != 0) {
	        ip = ipAddresses.split(",")[0];
	    }    //���ǲ��ܻ�ȡ���������ͨ��request.getRemoteAddr();��ȡ
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
	        ip = request.getRemoteAddr();
	    }
	    return ip;
	}

}
