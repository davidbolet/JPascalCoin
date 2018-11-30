package com.github.davidbolet.jpascalcoin.api.model;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NetStats implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	@SerializedName("active")
	@Expose
	protected Integer active;

	@SerializedName("clients")
	@Expose
	protected Integer clients;

	@SerializedName("servers")
	@Expose
	protected Integer servers;

	@SerializedName("servers_t")
	@Expose
	protected Integer serversT;

	@SerializedName("total")
	@Expose
	protected Integer total;

	@SerializedName("tclients")
	@Expose
	protected Integer totalClients;

	@SerializedName("tservers")
	@Expose
	protected Integer totalServers;

	@SerializedName("breceived")
	@Expose
	protected Long bytesReceived;

	@SerializedName("bsend")
	@Expose
	protected Long bytesSent;

	public Integer getActive() {
		return active;
	}

	public void setActive(Integer active) {
		this.active = active;
	}

	public Integer getClients() {
		return clients;
	}

	public void setClients(Integer clients) {
		this.clients = clients;
	}

	public Integer getServers() {
		return servers;
	}

	public void setServers(Integer servers) {
		this.servers = servers;
	}

	public Integer getServersT() {
		return serversT;
	}

	public void setServersT(Integer serversT) {
		this.serversT = serversT;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public Integer getTotalClients() {
		return totalClients;
	}

	public void setTotalClients(Integer totalClients) {
		this.totalClients = totalClients;
	}

	public Integer getTotalServers() {
		return totalServers;
	}

	public void setTotalServers(Integer totalServers) {
		this.totalServers = totalServers;
	}

	public Long getBytesReceived() {
		return bytesReceived;
	}

	public void setBytesReceived(Long bytesReceived) {
		this.bytesReceived = bytesReceived;
	}

	public Long getBytesSent() {
		return bytesSent;
	}

	public void setBytesSent(Long bytesSent) {
		this.bytesSent = bytesSent;
	}

}                         
