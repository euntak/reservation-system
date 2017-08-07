package kr.or.connect.reservation.utils;

public class FileUploadException extends Exception {
	private String message;
	
	public FileUploadException(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
}
