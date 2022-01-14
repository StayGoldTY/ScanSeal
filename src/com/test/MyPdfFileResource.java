package com.test;

import java.io.*;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import com.kinggrid.exception.KGServerInterfaceErrorException;
import com.kinggrid.pdf.web.IOUtils;
import com.kinggrid.pdfviewer.Contants;

import com.kinggrid.pdfviewer.PdfFileResource;

/**
 * 该接口实现为多实例，可以设置成员变量
 * 
 * 方法执行顺序
 *            1、getPdfFile
 *            2、getPdfFileStream
 *            3、getSavePdfFileSteam
 *            4、getDocumentName
 *            5、success
 * @author Administrator
 */
public class MyPdfFileResource extends PdfFileResource {
	
	private final static Map<String, String> PRE_MAP = new HashMap<String, String>();
	
	private String tmpFile;
	
	/**
	 * 返回需要盖章或查看PDF：文件模式
	 */
	public String getPdfFile(){
		// 如果文件流模式，该接口返回null
		String fileName = request.getServletContext().getRealPath("/files/" + documentId);



//		System.out.println("fail tmpFile = " +  tmpFile);
//
		InputStream in=null;
		FileOutputStream fileOutputStream = null;
		try{
			File file =new File(fileName);
			if(!file.exists()){
				file.createNewFile();
			}
			in=this.getPdfFileStream();
			fileOutputStream = new FileOutputStream(file);
			IOUtils.copy(in, fileOutputStream);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(),e);
		} finally {

			try {
				IOUtils.closeQuietly(in);
				IOUtils.closeQuietly(fileOutputStream);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return fileName;
		//return null;
	}

	/**
	 * 返回需要盖章或查看PDF：文件流模式
	 */
	public InputStream getPdfFileStream() {
		// 文件模式，返回null
//		String fileName = request.getServletContext().getRealPath("/files/" + documentId);
//		FileInputStream fileInputStream;
//		try {
//			fileInputStream = new FileInputStream(fileName);
//		} catch (FileNotFoundException e) {
//			throw new RuntimeException(e);
//		}
//		return fileInputStream;
//
		//String fileName = request.getSession().getServletContext().getRealPath("/files/" + documentId);
		OutputStream out=null;
		InputStream ins = null;
		ByteArrayOutputStream outStream = null;
		HttpURLConnection connection = null;
		try {
			URL url = new URL(Contants.getProperty("fileSericeUrl")+"DownFileStream?fileId="+documentId);
			connection = (HttpURLConnection)url.openConnection();
			connection.setConnectTimeout(10000);
			connection.setReadTimeout(10000);
			connection.connect();
			int code = connection.getResponseCode();
			if (code != 200) {
				throw new KGServerInterfaceErrorException("失败, code=" + code);
			}else{
				ins=connection.getInputStream();
			}
		}catch (ConnectException var13) {

			throw new KGServerInterfaceErrorException( "请求失败！");}
		catch (SocketException var14) {
			throw new KGServerInterfaceErrorException("网络异常, 请稍后再试！");
		} catch (IOException var15) {
			throw new RuntimeException(var15);
		}
		return ins;
	}

	/**
	 * 盖章后文件保存
	 */
	public OutputStream getSavePdfFileSteam() {
		String dir = request.getServletContext().getRealPath("/files");
		tmpFile = dir + File.separator + UUID.randomUUID().toString();
		FileOutputStream fileOutputStream;
		try {
			fileOutputStream = new FileOutputStream(tmpFile);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e.getMessage(),e);
		}
		return fileOutputStream;
	}

	/**
	 * 文档名称，记录日志时会使用
	 */
	public String getDocumentName(){
		return "document.pdf";
	}
	
	/**
	 *  加盖、删除印章等操作成功会执行该方法
	 *	1、可以实现覆盖原文档
	 *  2、删除原文档等
	 */
	public void success() {
		System.out.println("success action = " +  action);
		// 覆盖盖章后的文档
		if(tmpFile != null){
			try {
				saveFile();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 加盖、删除印章等操作失败时，删除临时文件
	 */
	public void fail() {
		System.out.println("fail action = " +  action);
		if(tmpFile != null){
			File file = new File(tmpFile);
			if(file.exists())file.delete();
		}
	}

	/**
	 * 覆盖原文档
	 */
	private void saveFile() throws IOException {
//		String dir = request.getServletContext().getRealPath("/files");
//		System.out.println("documentId = " + documentId);
//		System.out.println("userId = " + userId);
//		System.out.println(dir);//basefile/UploadFile
//		// 原文档路径
//		String fileName = dir + File.separator + documentId+".pdf";
//		FileOutputStream fileOutputStream = null;
//		FileInputStream in = null;
//		File file = new File(tmpFile);
//		try{
//			in = new FileInputStream(tmpFile);
//			fileOutputStream = new FileOutputStream(fileName);
//			IOUtils.copy(in, fileOutputStream);
//		} catch (IOException e) {
//			throw new RuntimeException(e.getMessage(),e);
//		} finally {
//			IOUtils.closeQuietly(in);
//			IOUtils.closeQuietly(fileOutputStream);
//			//if(file.exists()) file.delete();
//		}



//		String dir = request.getServletContext().getRealPath("/files");
//		System.out.println("documentId = " + documentId);
//		System.out.println("userId = " + userId);
//		System.out.println(dir);//basefile/UploadFile
//
//		// 原文档路径
//		String fileName = dir + File.separator + documentId;
//		FileOutputStream fileOutputStream = null;
//		FileInputStream in = null;
		//File file = new File(tmpFile);
//		try{
//			//in = new FileInputStream(tmpFile);
//
////			fileOutputStream = new FileOutputStream(fileName);
////			IOUtils.copy(in, fileOutputStream);
//		} finally {
////			IOUtils.closeQuietly(in);
////			IOUtils.closeQuietly(fileOutputStream);
//
//		}
//		CloseableHttpClient httpClient = HttpClients.createDefault();//1、创建实例
//		HttpPost uploadFile = new HttpPost(Contants.getProperty("fileSericeUrl")+"/basefile/UploadFile");//2、创建请求
//		String result="";
//		File f=null;
//					MultipartEntityBuilder builder = MultipartEntityBuilder.create();
//			//builder.addTextBody("text", "111", ContentType.TEXT_PLAIN);//传参
//			//builder.setCharset(Charset.forName("utf8"));//设置请求的编码格式
//			//builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);//设置浏览器兼容模式
//
////			// 把文件加到HTTP的post请求中
//			f = new File(tmpFile);
//
//			FileInputStream  in = new FileInputStream(f);
//
//		builder.addBinaryBody("pdf",in, ContentType.APPLICATION_OCTET_STREAM, f.getName());
//
//					HttpEntity multipart = builder.build();
//			uploadFile.setEntity(multipart);//对于HttpPost对象而言，可调用setEntity(HttpEntity entity)方法来设置请求参数。
//		CloseableHttpResponse response = httpClient.execute(uploadFile);

//		try {
//			builder.addBinaryBody(
//					"pdf",
//					new FileInputStream(f),
//					ContentType.APPLICATION_OCTET_STREAM,
//					f.getName());
//
//
//			HttpEntity multipart = builder.build();
//			uploadFile.setEntity(multipart);//对于HttpPost对象而言，可调用setEntity(HttpEntity entity)方法来设置请求参数。
//			response = httpClient.execute(uploadFile);
//
//			HttpEntity responseEntity = response.getEntity();//4、获取实体
//
//			result= EntityUtils.toString(responseEntity, "UTF-8");//5、获取网页内容，并且指定编码
//			response.close();
//			httpClient.close();
//
//
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		}catch (IOException e) {
//			e.printStackTrace();
//		}finally {
//
//		}


		//System.out.println("Post 返回结果"+result);

//		try{
//
//
//			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
//			//builder.addTextBody("text", "111", ContentType.TEXT_PLAIN);//传参
//			//builder.setCharset(Charset.forName("utf8"));//设置请求的编码格式
//			//builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);//设置浏览器兼容模式
//
//			// 把文件加到HTTP的post请求中
//			f = new File(tmpFile);
//			builder.addBinaryBody(
//					"pdf",
//					new FileInputStream(f),
//					ContentType.APPLICATION_OCTET_STREAM,
//					f.getName()
//			);
//
//			HttpEntity multipart = builder.build();
//			uploadFile.setEntity(multipart);//对于HttpPost对象而言，可调用setEntity(HttpEntity entity)方法来设置请求参数。
//			CloseableHttpResponse response = httpClient.execute(uploadFile);//3、执行
//			HttpEntity responseEntity = response.getEntity();//4、获取实体
//			//打印内容
//			result= EntityUtils.toString(responseEntity, "UTF-8");//5、获取网页内容，并且指定编码
//			System.out.println("Post 返回结果"+result);
//			httpClient.close();
//			response.close();
//		}catch(Exception ex){
//			ex.printStackTrace();
//		}finally{
//			if(f.exists()) f.delete();
//		}

		HttpUtil.saveRemoteFile(tmpFile,documentId);

		HttpUtil.FlagDocStamp(documentId);
	}
	
	/**
	 * 域签名、电子签章，密钥盘签名时会先执行该方法，
	 * 1、实现保存盖章未签名的文档
	 * 2、签名完成后，需要在未签名的文档上（通过getPrePdfFile）加上签名值，覆盖该文档
	 * 3、再执行success
	 */
	public void preSuccess(){
		// 最好使用缓存机制（如：ehcache）, 超过30秒删除该缓存
		PRE_MAP.put(documentId, tmpFile);
	}
	
	public String getPrePdfFile(){
		tmpFile = PRE_MAP.remove(documentId);
		return tmpFile;
	}

	@Override
	public List<String> getGBEsids() {
		List<String> list = new ArrayList<String>();
		list.add("36000000000278");
		return list;
	}

}
