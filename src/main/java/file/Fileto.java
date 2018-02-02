package file;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.aliyun.oss.OSSClient;

/**
 * Servlet implementation class Fileto
 */
public class Fileto extends HttpServlet {
	private static final Logger logger =LogManager.getLogger(Fileto.class.getName());

	protected void doGet(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		// 在解析请求之前先判断请求类型是否为文件上传类型
		boolean isMultipart = ServletFileUpload.isMultipartContent(req);
		String status = "not OK";
		// 文件上传处理工厂
		FileItemFactory factory = new DiskFileItemFactory();
		// 创建文件上传处理器
		ServletFileUpload upload = new ServletFileUpload(factory);
		//设置编码，获取的FileItem时，还需要转码,(经测试只需在获取时设置就行）
		//upload.setHeaderEncoding("utf-8");  
		// 开始解析请求信息
		List items = null;
		try {
			items = upload.parseRequest(req);
		} catch (FileUploadException e) {
			logger.error(e.getMessage());
		}
		// 对所有请求信息进行判断
		Iterator iter = items.iterator();
			while (iter.hasNext()) {
				FileItem item = (FileItem) iter.next();
				// 信息为普通的格式
				if (item.isFormField()) {
					String fieldName = item.getFieldName();
					logger.info("转码前文件名"+item.getString());
					//获取的FileItem执行如下转码方式
					String value = item.getString("utf-8"); 
					logger.info("转码后文件名"+value);
					req.setAttribute(fieldName, value);
				}   
				// 信息为文件格式
				else if(item.getName().length()>0){
					String fileName = item.getName();
					InputStream inputStream = item.getInputStream();
					// 将文件写入
					String endpoint = "http://oss-cn-shenzhen.aliyuncs.com";
					// 云账号AccessKey有所有API访问权限，建议遵循阿里云安全最佳实践，创建并使用RAM子账号进行API访问或日常运维，请登录
					// ht tps://ram.console.aliyun.com 创建
					String accessKeyId = "LTAIvTthEd7SfAPh";
					String accessKeySecret = "1MbHX744NDwFfW7yEptCEvurdGgq73";
					// 创建OSSClient实例
					OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
					// 上传文件
					ossClient.putObject("lanjian666", fileName, inputStream);
					// 关闭client
					ossClient.shutdown();
					status = "I'm OK";
				}
			}
		req.setAttribute("status", status);
		req.getRequestDispatcher("AreYouOK.jsp").forward(req, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
