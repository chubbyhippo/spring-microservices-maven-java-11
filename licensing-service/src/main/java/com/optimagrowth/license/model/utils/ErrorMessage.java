package com.optimagrowth.license.model.utils;

public class ErrorMessage {

	private String message;
	private String code;
	private String detail;

	public ErrorMessage(String message, String code, String detail) {
		super();
		this.message = message;
		this.code = code;
		this.detail = detail;
	}

	public ErrorMessage(String message, String detail) {
		super();
		this.message = message;
		this.detail = detail;
	}

	public ErrorMessage(String message) {
		this(message, "", "");
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

}
