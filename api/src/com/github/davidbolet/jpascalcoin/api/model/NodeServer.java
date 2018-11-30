package com.github.davidbolet.jpascalcoin.api.model;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NodeServer implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SerializedName("ip")
	@Expose
	protected String IP;

	@SerializedName("port")
	@Expose
	protected Short port;

	@SerializedName("lastcon")
	@Expose
	protected Long lastConnect;

	@SerializedName("attempts")
	@Expose
	protected Integer attempts;

	public String getIP() {
		return IP;
	}

	public void setIP(String iP) {
		IP = iP;
	}

	public Short getPort() {
		return port;
	}

	public void setPort(Short port) {
		this.port = port;
	}

	public Long getLastConnect() {
		return lastConnect;
	}

	public void setLastConnect(Long lastConnect) {
		this.lastConnect = lastConnect;
	}

	public Integer getAttempts() {
		return attempts;
	}

	public void setAttempts(Integer attempts) {
		this.attempts = attempts;
	}
	
	
}
