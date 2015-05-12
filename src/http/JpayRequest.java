package http;

import http.response.JpayBaseResp;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;

import com.google.gson.Gson;

public abstract class JpayRequest<T extends JpayBaseResp> {
	private String url;
	private HashMap<String, String> params;
	private HashMap<String, String> headers;
	private RequestType type;
	private Type clazz;

	

	static Gson gson = new Gson();

	public JpayRequest(String url, HashMap<String, String> headers,
			HashMap<String, String> params, RequestType type) {
		super();
		this.url = url;
		this.headers = headers;
		this.params = params;
		this.type = type;
		this.clazz = ((ParameterizedType) getClass().getGenericSuperclass())
				.getActualTypeArguments()[0];
	}

	@SuppressWarnings("unchecked")
	public T request() throws Exception {
		String strResp = null;
		switch (type) {
		case POST:
			strResp = http.HttpClient.post(url, headers, params);
			break;
		case GET:
			strResp = http.HttpClient.get(url, params);
			break;
		}

		T data = null;
		try {
			data = gson.fromJson(strResp, (Class<T>) clazz);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}

}
