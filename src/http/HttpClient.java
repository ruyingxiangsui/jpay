package http;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

public class HttpClient {

	private static int BUFFER_SIZE = 1024;

	public static String get(String url, Map<String, String> headers,
			Map<String, String> params) {

		// 先将参数放入List，再对参数进行URL编码
		List<BasicNameValuePair> basicParams = new LinkedList<BasicNameValuePair>();
		for (Entry<String, String> param : params.entrySet()) {
			basicParams.add(new BasicNameValuePair(param.getKey(), param
					.getValue()));
		}
		// 对参数编码
		String param = URLEncodedUtils.format(basicParams, "UTF-8");
		// 将URL与参数拼接
		HttpGet get = new HttpGet(url + "?" + param);
		// 添加头信息
		for (Entry<String, String> header : headers.entrySet()) {
			get.addHeader(header.getKey(), header.getValue());
		}
		org.apache.http.client.HttpClient httpClient = new DefaultHttpClient();

		try {
			HttpResponse response = httpClient.execute(get); // 发起GET请求
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != 200) {
				throw new Exception("请求出错");
			}
			InputStream is = response.getEntity().getContent();
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			byte[] bytes = new byte[BUFFER_SIZE];
			int count = -1;
			while ((count = is.read(bytes, 0, BUFFER_SIZE)) != -1) {
				out.write(bytes, 0, count);
			}
			return new String(out.toByteArray(), "UTF-8");

		} catch (Exception e) {
			new Exception("网络无法连接，请检查网络设置");
			e.printStackTrace();
		}

		return null;
	}

	public static String post(String url, Map<String, String> headers,
			Map<String, String> params) {
		HttpPost post = new HttpPost(url);

		// 添加头信息
		for (Entry<String, String> header : headers.entrySet()) {
			post.addHeader(header.getKey(), header.getValue());
		}

		// 将参数放入List，再对参数进行URL编码
		JSONObject json = new JSONObject();
		for (Entry<String, String> param : params.entrySet()) {
			try {
				json.put(param.getKey(), param.getValue());
			} catch (JSONException e) {
				e.printStackTrace();
				return null;
			}
		}

		// 将参数放入request body
		try {
			post.setEntity(new StringEntity(json.toString(), "UTF-8"));
		} catch (UnsupportedEncodingException e) {

			e.printStackTrace();
		}

		org.apache.http.client.HttpClient httpClient = new DefaultHttpClient();

		try {
			HttpResponse response = httpClient.execute(post);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != 200) {
				throw new Exception("请求出错："+url);
			}
			InputStream is = response.getEntity().getContent();
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			byte[] bytes = new byte[BUFFER_SIZE];
			int count = -1;
			while ((count = is.read(bytes, 0, BUFFER_SIZE)) != -1) {
				out.write(bytes, 0, count);
			}
			return new String(out.toByteArray(), "UTF-8");

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static String put(String url, Map<String, String> headers,
			Map<String, String> params) {
		org.apache.http.client.HttpClient httpClient = new DefaultHttpClient();
		HttpPut put = new HttpPut(url);
		// 将参数放入List，再对参数进行URL编码
		JSONObject json = new JSONObject();
		for (Entry<String, String> param : params.entrySet()) {
			try {
				json.put(param.getKey(), param.getValue());
			} catch (JSONException e) {
				e.printStackTrace();
				return null;
			}
		}
		try {
			put.setEntity(new StringEntity(json.toString(), "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			HttpResponse response = httpClient.execute(put);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != 200) {
				throw new Exception("请求出错");
			}
			InputStream is = response.getEntity().getContent();
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			byte[] bytes = new byte[BUFFER_SIZE];
			int count = -1;
			while ((count = is.read(bytes, 0, BUFFER_SIZE)) != -1) {
				out.write(bytes, 0, count);
			}
			return new String(out.toByteArray(), "UTF-8");

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

}
