package com.github.davidbolet.jpascalcoin.api.model;

import java.io.Serializable;

import com.github.davidbolet.jpascalcoin.exception.RPCApiException;
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
	private RPCApiException error;

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
	
	public RPCApiException getError()
	{
		return this.error;
	}
	
	public void setError(RPCApiException error)
	{
		this.error = error;
	}
	
	public String getErrorMessage()
	{
		if (this.isError())
			return this.error.getMessage();
		else
			return "";
	}
	
	
}
