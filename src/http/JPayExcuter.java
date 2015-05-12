package http;

import http.response.JpayBaseResp;
import android.os.AsyncTask;

public class JPayExcuter<T extends JpayBaseResp> extends AsyncTask<Object,Float,T> {

	private JpayRequest<T> request;
	private JPayRequestListener<T> listener;
	private Exception ex;
	
	public JPayExcuter(JpayRequest<T> request, JPayRequestListener<T> listener) {
		super();
		this.request = request;
		this.listener = listener;
	}

	@Override
	protected T doInBackground(Object... params) {
		try {
			return request.request();
		} catch (Exception e) {
			this.ex = e;
		}
		return null;
	}
	
	@Override
	protected void onPostExecute(T result) {
		if(ex!=null){
			listener.onRequestFailed(ex.toString());
			return;
		}
		if(result == null){
			listener.onRequestFailed("data error!");
			return;
		}
		listener.onRequestSucceeded(result);
	}

}
