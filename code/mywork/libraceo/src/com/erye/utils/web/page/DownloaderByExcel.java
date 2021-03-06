package com.erye.utils.web.page;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.List;
import java.util.Scanner;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.erye.utils.common.ReadExcel;
import com.erye.utils.common.VeDate;

/***
 * 本类用于将指定url对应的网页下载至本地�?��文件�?
 * 
 * @author libra
 *
 */
public class DownloaderByExcel {
	
	public static void downloadPageByGetMethod(String url, String[] arg) throws IOException {

		// 1、�?过HttpGet获取到response对象
		CloseableHttpClient httpClient = HttpClients.createDefault();
		// 注意，必�?��加上http://的前�?��否则会报：Target host is null异常�?
		HttpGet httpGet = new HttpGet(url);
		CloseableHttpResponse response = httpClient.execute(httpGet);

		InputStream is = null;
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			try {
				// 2、获取response的entity�?
				HttpEntity entity = response.getEntity();

				// 3、获取到InputStream对象，并对内容进行处�?
				is = entity.getContent();

				String fileName = getFileName(arg);
				
				saveToFile(saveUrl, fileName, is);
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} finally {

				if (is != null) {
					is.close();
				}
				if (response != null) {
					response.close();
				}
			}
		}
	}

	//将输入流中的内容输出到path指定的路径，fileName指定的文件名
	private static void saveToFile(String path, String fileName, InputStream is) {
		Scanner sc = new Scanner(is);
		Writer os = null;
		try {
			os = new PrintWriter(path + fileName);
			while (sc.hasNext()) {
				os.write(sc.nextLine());
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (sc != null) {
				sc.close();
			}
			if (os != null) {
				try{
				os.flush();
				os.close();
				}catch(IOException e){
					e.printStackTrace();
					System.out.println("输出流关闭失败！");
				}
			}
		}
	}

	// 将url中的特殊字符用下划线代替
	public static String getFileName(String url) {
		url = url.substring(7);
		String fileName = url.replaceAll("[\\?:*|<>\"/]", "_") + ".html";
		return fileName;
	}
	
	
	// 将url中的特殊字符用下划线代替
	public static String getFileName(String[] info) {
		String url = info[0].trim() + TAG__ + info[1].trim() + TAG__ + info[5].trim() + TAG__ + info[6];
		String fileName = url.replaceAll("[\\?:*|<>\"/]", "_") + TAG_HTML;
		return fileName;
	}
	
	
	public static void main(String[] args) {
		try {
			List<String[]> list = ReadExcel.readExcel(url);
			for (int i = 0; i < list.size(); i++) {
				String[] str = list.get(i);
				String url = "";
				if (TAG_NEW.equals(str[6])) {
					url = TAG_NEW_URL + str[0].trim();
				}
				if (TAG_OLD.equals(str[6])) {
					url = str[3];
				}
				str[4] = VeDate.getDateFormat(str[4]);
				str[5] = VeDate.getDateFormat(str[5]);
				downloadPageByGetMethod(url, str);
				System.out.println(i);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static String TAG_NEW = "new";

	static String TAG_OLD = "old";

	static String TAG__ = "_";
	
	static String TAG_HTML = ".html";
	
	static String url = "E:\\百度云同步盘\\工作相关\\公司项目\\658金融\\工作备案\\2015-09-25【数据导出�?\\9-25—�?10-07.xls";
	
	static String TAG_NEW_URL = "http://www.658.com/userTrade/contractDetail/financeID/";
	
	static String saveUrl = "E:\\百度云同步盘\\工作相关\\公司项目\\658金融\\工作备案\\2015-09-25【数据导出�?\\9-25—�?10-07\\";
}

