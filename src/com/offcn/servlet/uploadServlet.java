package com.offcn.servlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 * Servlet implementation class uploadServlet
 */
public class uploadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public uploadServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		File[] files=uploadFile(request,response);
		for(File f:files){
			if(f!=null){
		response.getWriter().append("File name:"+f.getName()).append('\n');
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	public File[] uploadFile(HttpServletRequest request, HttpServletResponse response) throws IOException{
		String webroot = this.getServletContext().getRealPath("/");
		File temppath = new File(webroot + "fileuploadtemp");
		File uploadpath = new File(webroot + "fileupload");
		if (!temppath.exists()) {
			temppath.mkdirs();
		}
		if (!uploadpath.exists()) {
			uploadpath.mkdirs();
		}
        //声明diskfileitemfactory工厂类，用于在指的磁盘上设置一个临时目录
		DiskFileItemFactory factory = new DiskFileItemFactory(1024 * 1024,temppath);
		//声明ServletFileUpoload，接收上面的临时目录
		ServletFileUpload upload = new ServletFileUpload(factory);
		//设置上传文件最大值
		upload.setFileSizeMax(1024 * 1024 * 100);
		File[] Rfile=new File[2];
		try {
			//解析request，获得上传的数据结果集
			List<FileItem> fileItems = upload.parseRequest(request);
			//迭代遍历结果集
			Iterator<FileItem> it = fileItems.iterator();
			int k=0;
			while (it.hasNext()) {
				FileItem fi = it.next();
				//判断是否为表单元素，是的话解析表单元素
				if (fi.isFormField()) {
					System.out.println("字段名：" + fi.getFieldName());
					System.out.println("字段值：" + fi.getString());
				} else {
					  String ym = new SimpleDateFormat("yyyy-MM-ddhhmmss").format(new Date());
					  String filePath = uploadpath+"/" + ym + fi.getName();
					  
					  System.out.println("Upload  File Name:" + ym);						
				//判断为上传文件、调用获取输入流
					InputStream in = fi.getInputStream();
					File fileout=new File(filePath);
					//准备输出流
					FileOutputStream out = new FileOutputStream(fileout);
					byte buffer[] = new byte[1024];
					int len = 0;
					//用缓冲区方式循环把输入流写入到输出流
					while ((len = in.read(buffer)) > 0) {
						out.write(buffer, 0, len);
					}
					//关闭输入输出流
					in.close();
					out.close();
					fi.delete();
					
					Rfile[k]=fileout;
					k++;
					
					
				}
			}
			return Rfile;
		} catch (FileUploadException e) {
			response.getWriter().write(e.toString());
		}
		return null;
	}
}
