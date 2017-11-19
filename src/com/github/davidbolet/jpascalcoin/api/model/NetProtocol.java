package com.github.davidbolet.jpascalcoin.api.model;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NetProtocol implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Net protocol version
	 */
	@SerializedName("ver")
	@Expose
	protected Integer version;

	/**
	 * Net protocol available
	 */
	@SerializedName("ver_a")
	@Expose
	protected Integer availableVersion;

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Integer getAvailableVersion() {
		return availableVersion;
	}

	public void setAvailableVersion(Integer availableVersion) {
		this.availableVersion = availableVersion;
	}
}
