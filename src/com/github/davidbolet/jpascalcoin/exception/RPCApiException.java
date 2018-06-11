package com.github.davidbolet.jpascalcoin.exception;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RPCApiException extends RuntimeException {
 
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@SerializedName("code")
    @Expose
	Integer code;
	
	@SerializedName("message")
    @Expose
	String message;
	
	public RPCApiException(String message) {
		super(message);
		this.message=message;
	}
	
	public RPCApiException(Integer code, String message) {
		super(message);
		this.message=message;
		this.code=code;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	

}
