package org.jpascalcoin.api.model;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OpResult<T> implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@SerializedName("result")
    @Expose
	private T result;
	
	@SerializedName("error")
    @Expose
	private Error error;

	public T getResult() {
		return result;
	}

	public void setResult(T result) {
		this.result = result;
	}
	
	public boolean isError()
	{
		return getError()!=null;
	}
	
	public Error getError()
	{
		return this.error;
	}
	
	public void setError(Error error)
	{
		this.error = error;
	}
	
	public String getErrorMessage()
	{
		if (this.isError())
			return this.error.message;
		else
			return "";
	}
	
	public static class Error {
	    String message;
	    
	}
	
}
