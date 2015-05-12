package http;

public interface JPayRequestListener<T> {
	
	public void onRequestSucceeded(T data);

	public void onRequestFailed(String ex);
}
