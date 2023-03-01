package pojo;

public class ViewOrderResponse {
	private DataDetail Data;
	private String message;

	public DataDetail getData() {
		return Data;
	}

	public void setData(DataDetail data) {
		Data = data;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
