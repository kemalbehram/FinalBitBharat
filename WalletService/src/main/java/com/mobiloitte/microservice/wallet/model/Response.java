package com.mobiloitte.microservice.wallet.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * The class Response.
 * @author Ankush Mohapatra
 */
public class Response<T> {
	private int status;
	private String message;
	@JsonInclude(value=Include.NON_NULL)
	private T data;
	private Date timestamp;

	public static final Response<?> SUCCESS = new Response<>(200, "Success");
	public static final Response<?> CREATED = new Response<>(201, "Created");
	public static final Response<?> FAILURE = new Response<>(205, "Failed");
	public static final Response<?> NOT_FOUND = new Response<>(404, "Resource not found");
	public static final Response<?> BAD_REQUEST = new Response<>(400, "Bad Request");
	public static final Response<?> SERVER_ERROR = new Response<>(500, "Server Error");

	public Response(int statusCode, String message, T data) {
		this();
		this.status = statusCode;
		this.message = message;
		this.data = data;
	}

	public Response(int statusCode, String message) {
		this();
		this.status = statusCode;
		this.message = message;
	}

	public Response(T data) {
		this(200, "Success");
		this.data = data;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "Response [statusCode=" + status + ", message=" + message + ", data=" + data + "]";
	}

	public Response() {
		super();
		this.timestamp = new Date();
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

}
