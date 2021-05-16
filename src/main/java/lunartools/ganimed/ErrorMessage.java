package lunartools.ganimed;

public class ErrorMessage {
	private String message;
	
	public ErrorMessage(String message) {
		this.message=message;
	}
	
	public String getMessage() {
		return message;
	}
	
	public String toString() {
		return ErrorMessage.class.getSimpleName()+": "+message;
	}
	
}
