package com.usermanagement.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MongoConfigProperties {
	
	@Value("${spring.data.mongodb.host:127.0.0.1}")
	private String host;
	
	@Value("${spring.data.mongodb.port:27017}")
	private int port;
	
	@Value("${spring.data.mongodb.database:userManagementDataBase}")
	private String userManagementDatabaseName;
	
	@Value("${spring.data.mongodb.pagesize:1000}")
	private Integer pagesize;
	
	@Value("${spring.data.mongodb.username}")
	private String  userName;

	@Value("${spring.data.mongodb.password}")
	private String  password;

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getUserManagementDatabaseName() {
		return userManagementDatabaseName;
	}

	public void setUserManagementDatabaseName(String userManagementDatabaseName) {
		this.userManagementDatabaseName = userManagementDatabaseName;
	}

	public Integer getPagesize() {
		return pagesize;
	}

	public void setPagesize(Integer pagesize) {
		this.pagesize = pagesize;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
