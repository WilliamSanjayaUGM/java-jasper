package com.manulife.java_jasper.exception;

public class ReportGenerateException extends RuntimeException{
	public ReportGenerateException(String message) {
		super(message);
	}
	
	public ReportGenerateException(String message, Throwable cause) {
		super(message, cause);
	}
}
