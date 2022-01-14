package com.test;
import com.kinggrid.pdfviewer.Contants;
import org.apache.http.HttpEntity;

import org.apache.http.client.methods.HttpPost;

import java.io.FileInputStream;
import org.apache.http.impl.client.HttpClients;

import org.apache.http.util.EntityUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.entity.mime.HttpMultipartMode;
import java.io.File;
public  class HttpUtil {
    public static String  saveRemoteFile(String tempfile,String documentId) {
        String result="";
        File f=null;
        try{
            CloseableHttpClient httpClient = HttpClients.createDefault();//1、创建实例
            HttpPost uploadFile = new HttpPost(Contants.getProperty("fileSericeUrl")+"uploadsinglepdf");//2、创建请求

            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.addTextBody("DocId", documentId, ContentType.TEXT_PLAIN);//传参
            //builder.setCharset(Charset.forName("utf8"));//设置请求的编码格式
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);//设置浏览器兼容模式

            // 把文件加到HTTP的post请求中
            f = new File(tempfile);
            builder.addBinaryBody(
                    "pdf",
                    new FileInputStream(f),
                    ContentType.APPLICATION_OCTET_STREAM,
                    f.getName()
            );

            HttpEntity multipart = builder.build();
            uploadFile.setEntity(multipart);//对于HttpPost对象而言，可调用setEntity(HttpEntity entity)方法来设置请求参数。
            CloseableHttpResponse response = httpClient.execute(uploadFile);//3、执行
            HttpEntity responseEntity = response.getEntity();//4、获取实体
            //打印内容
            result= EntityUtils.toString(responseEntity, "UTF-8");//5、获取网页内容，并且指定编码
            System.out.println("Post 返回结果"+result);
            httpClient.close();
            response.close();
        }catch(Exception ex){
            ex.printStackTrace();
        }finally{
            if(f.exists()) f.delete();
        }
        return result;
    }

    public static String  FlagDocStamp(String documentId) {
        String result="";
        File f=null;
        try{
            CloseableHttpClient httpClient = HttpClients.createDefault();//1、创建实例
            HttpPost httpPost = new HttpPost(Contants.getProperty("appserverUrl"));//2、创建请求
            httpPost.addHeader("Content-Type", "application/json;charset=UTF-8");
            String  params= "{\"FileCode\":\""+documentId+"\"}";
            httpPost.setEntity(new StringEntity(params));
            CloseableHttpResponse response = httpClient.execute(httpPost);//3、执行

            HttpEntity responseEntity = response.getEntity();//4、获取实体
            //打印内容
            result= EntityUtils.toString(responseEntity, "UTF-8");//5、获取网页内容，并且指定编码
            System.out.println("Post 返回结果"+result);
            httpClient.close();
            response.close();
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return result;
    }


}

