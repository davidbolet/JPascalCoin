package com.github.davidbolet.jpascalcoin.api.model;

public class ErrorResponse {
	  Error error;
	  
	  public static class Error {
	    Data data;
	  
	    public static class Data {
	       String message;
	    }  
	  }
	}
