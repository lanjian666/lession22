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
		// �ڽ�������֮ǰ���ж����������Ƿ�Ϊ�ļ��ϴ�����
		boolean isMultipart = ServletFileUpload.isMultipartContent(req);
		String status = "not OK";
		// �ļ��ϴ�������
		FileItemFactory factory = new DiskFileItemFactory();
		// �����ļ��ϴ�������
		ServletFileUpload upload = new ServletFileUpload(factory);
		//���ñ��룬��ȡ��FileItemʱ������Ҫת��,(������ֻ���ڻ�ȡʱ���þ��У�
		//upload.setHeaderEncoding("utf-8");  
		// ��ʼ����������Ϣ
		List items = null;
		try {
			items = upload.parseRequest(req);
		} catch (FileUploadException e) {
			logger.error(e.getMessage());
		}
		// ������������Ϣ�����ж�
		Iterator iter = items.iterator();
			while (iter.hasNext()) {
				FileItem item = (FileItem) iter.next();
				// ��ϢΪ��ͨ�ĸ�ʽ
				if (item.isFormField()) {
					String fieldName = item.getFieldName();
					logger.info("ת��ǰ�ļ���"+item.getString());
					//��ȡ��FileItemִ������ת�뷽ʽ
					String value = item.getString("utf-8"); 
					logger.info("ת����ļ���"+value);
					req.setAttribute(fieldName, value);
				}   
				// ��ϢΪ�ļ���ʽ
				else if(item.getName().length()>0){
					String fileName = item.getName();
					InputStream inputStream = item.getInputStream();
					// ���ļ�д��
					String endpoint = "http://oss-cn-shenzhen.aliyuncs.com";
					// ���˺�AccessKey������API����Ȩ�ޣ�������ѭ�����ư�ȫ���ʵ����������ʹ��RAM���˺Ž���API���ʻ��ճ���ά�����¼
					// ht tps://ram.console.aliyun.com ����
					String accessKeyId = "LTAIvTthEd7SfAPh";
					String accessKeySecret = "1MbHX744NDwFfW7yEptCEvurdGgq73";
					// ����OSSClientʵ��
					OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
					// �ϴ��ļ�
					ossClient.putObject("lanjian666", fileName, inputStream);
					// �ر�client
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
