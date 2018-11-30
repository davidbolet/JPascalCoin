package com.github.davidbolet.jpascalcoin.api.model;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Connection implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * True if this connection is to a server node.False if this connection is a client node
	*/
	@SerializedName("server")
	@Expose
	protected Boolean isServer;

	@SerializedName("ip")
	@Expose
	protected String IP;

	@SerializedName("port")
	@Expose
	protected Short port;

	/**
	 * seconds of live of this connection
	 */
	@SerializedName("secs")
	@Expose
	protected Integer connectedDurationSec;
	
	/**
	 * Bytes sent
	 */
	@SerializedName("sent")
	@Expose
	protected Long bytesSent;

	/**
	 * Bytes received
	 */
	@SerializedName("recv")
	@Expose
	protected Long bytesReceived;

	/**
	 * Other node App version
	 */
	@SerializedName("appver")
	@Expose
	protected String appVersion;

	/**
	 * Net protocol of other node
	 */
	@SerializedName("netvar")
	@Expose
	protected Integer remoteVersion;

	/**
	 * Net protocol available of other node
	 */
	@SerializedName("netvar_a")
	@Expose
	protected Integer removeAvailableVersion;

	/**
	 * Net timediff of other node (vs wallet)
	 */
	@SerializedName("timediff")
	@Expose
	protected int timeDiff;

	public Boolean getIsServer() {
		return isServer;
	}

	public void setIsServer(Boolean isServer) {
		this.isServer = isServer;
	}

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

	public Integer getConnectedDurationSec() {
		return connectedDurationSec;
	}

	public void setConnectedDurationSec(Integer connectedDurationSec) {
		this.connectedDurationSec = connectedDurationSec;
	}

	public Long getBytesSent() {
		return bytesSent;
	}

	public void setBytesSent(Long bytesSent) {
		this.bytesSent = bytesSent;
	}

	public Long getBytesReceived() {
		return bytesReceived;
	}

	public void setBytesReceived(Long bytesReceived) {
		this.bytesReceived = bytesReceived;
	}

	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	public Integer getRemoteVersion() {
		return remoteVersion;
	}

	public void setRemoteVersion(Integer remoteVersion) {
		this.remoteVersion = remoteVersion;
	}

	public Integer getRemoveAvailableVersion() {
		return removeAvailableVersion;
	}

	public void setRemoveAvailableVersion(Integer removeAvailableVersion) {
		this.removeAvailableVersion = removeAvailableVersion;
	}

	public int getTimeDiff() {
		return timeDiff;
	}

	public void setTimeDiff(int timeDiff) {
		this.timeDiff = timeDiff;
	}
}
