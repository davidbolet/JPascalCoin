package com.github.davidbolet.jpascalcoin.api.model;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NodeStatus implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** 
	 * Must be true, otherwise Node is not ready to execute operations
	 */
	@SerializedName("ready")
	@Expose
	protected Boolean ready;

	/** 
	 *Human readable information about ready or not
	 */
	@SerializedName("ready_s")
	@Expose
	protected String readyDescriptor;


	/** 
	 * Human readable information about node status...Running, downloading blockchain, discovering servers...
	 */
	@SerializedName("status_s")
	@Expose
	protected String statusDescriptor;

	/** 
	 * Server port
	 */
	@SerializedName("port")
	@Expose
	protected Short port;

	 /** 
	 * True when this wallet is locked, false otherwise
	 */
	@SerializedName("locked")
	@Expose
	protected Boolean locked;

	/** 
	 * Timestamp of the Node
	 */
	@SerializedName("timestamp")
	@Expose
	protected Long timestamp;

	/** 
	 * Server version
	 */
	@SerializedName("version")
	@Expose
	protected String version;

	/** 
	 * JSON Object with protocol versions
	 */
	@SerializedName("netprotocol")
	@Expose
	protected NetProtocol netProtocol;

	/** 
	 * Blockchain blocks
	 */
	@SerializedName("blocks")
	@Expose
	protected Integer blocks;

	/** 
	 * JSON Object with net information
	 */
	@SerializedName("netstats")
	@Expose
	protected NetStats netStats;


	/** 
	 * JSON Array with servers candidates
	 */
	@SerializedName("nodeservers")
	@Expose
	protected List<NodeServer> nodeServers;
	
	
	/**
	 *  OpenSSL library version as described in OpenSSL_version_num 
	 *  ( https://www.openssl.org/docs/man1.1.0/crypto/OPENSSL_VERSION_NUMBER.html )
	 */
	@SerializedName("openssl")
	@Expose
	protected String openssl;


	public Boolean getReady() {
		return ready;
	}


	public void setReady(Boolean ready) {
		this.ready = ready;
	}


	public String getReadyDescriptor() {
		return readyDescriptor;
	}


	public void setReadyDescriptor(String readyDescriptor) {
		this.readyDescriptor = readyDescriptor;
	}


	public String getStatusDescriptor() {
		return statusDescriptor;
	}


	public void setStatusDescriptor(String statusDescriptor) {
		this.statusDescriptor = statusDescriptor;
	}


	public Short getPort() {
		return port;
	}


	public void setPort(Short port) {
		this.port = port;
	}


	public Boolean getLocked() {
		return locked;
	}


	public void setLocked(Boolean locked) {
		this.locked = locked;
	}


	public Long getTimestamp() {
		return timestamp;
	}


	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}


	public String getVersion() {
		return version;
	}


	public void setVersion(String version) {
		this.version = version;
	}


	public NetProtocol getNetProtocol() {
		return netProtocol;
	}


	public void setNetProtocol(NetProtocol netProtocol) {
		this.netProtocol = netProtocol;
	}


	public Integer getBlocks() {
		return blocks;
	}


	public void setBlocks(Integer blocks) {
		this.blocks = blocks;
	}


	public NetStats getNetStats() {
		return netStats;
	}


	public void setNetStats(NetStats netStats) {
		this.netStats = netStats;
	}


	public List<NodeServer> getNodeServers() {
		return nodeServers;
	}


	public void setNodeServers(List<NodeServer> nodeServers) {
		this.nodeServers = nodeServers;
	}


	public String getOpenssl() {
		return openssl;
	}


	public void setOpenssl(String openssl) {
		this.openssl = openssl;
	}
	
	
}
