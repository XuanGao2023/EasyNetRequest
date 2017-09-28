package com.pan.commlib.core;


import com.pan.commlib.utils.DataConvertUtils;
import com.pan.commlib.utils.MiscUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Request {
	private static final Integer DEFAULT_TIMEOUT = 5000;

	protected Method requestMethod = Method.GET;
	protected String strRequestHost = "";
	protected String strRequestCmd = "";
	protected Map<String, String> mapRequestParams = new HashMap<>();
	protected Map<String, String> mapRequestHeaders = new HashMap<>();
	protected Integer nTimeout = DEFAULT_TIMEOUT;
	private String strUrl = "";
	private byte[] bytesBody = null;

	public Request(byte[] body) {
		if (body != null) {
			this.bytesBody = body;
		}
		init();
	}

	public void init() {

	}

	protected static String buildRequestUrl(String host, String cmd, Map<String, String> params) {
		String requestUrl = host;
		if (MiscUtils.isEmpty(requestUrl)) {
			throw new IllegalArgumentException("requestUrl is invalid!");
		}

		if (!MiscUtils.isEmpty(cmd)) {
			//If requestUrl doesn't end with '/' then add to it.
			if (!requestUrl.endsWith("/")) {
				requestUrl += "/";
			}
			//If cmd starts with '/' then eliminate it.
			if (cmd.startsWith("/")) {
				cmd = cmd.substring(1, cmd.length());
			}
			requestUrl += cmd;
			if (params != null && !params.isEmpty()) {
				requestUrl += "?" + getRequestParam(params);
			}
		} else {
			if (params != null && !params.isEmpty()) {
				if (!requestUrl.contains("?"))
					requestUrl += "?" + getRequestParam(params);
				else
					requestUrl += "&" + getRequestParam(params);
			}
		}
		return requestUrl;
	}

	protected static String getRequestParam(Map<String, String> mapRequestParams) {
		return getRequestParam(mapRequestParams, false);
	}

	protected static String getRequestParam(Map<String, String> mapRequestParams, boolean encoded) {
		StringBuilder strbuilder = new StringBuilder();
		Iterator entries = mapRequestParams.entrySet().iterator();
		while (entries.hasNext()) {
			Map.Entry entry = (Map.Entry) entries.next();
			strbuilder.append(entry.getKey());
			strbuilder.append("=");
			if (encoded) {
				strbuilder.append(URLEncode(entry.getValue().toString()));
			} else {
				strbuilder.append(entry.getValue());
			}

			if (entries.hasNext())
				strbuilder.append("&");
		}
		return strbuilder.toString();
	}

	public static String URLEncode(String str) {
		try {
			String strencoded = URLEncoder.encode(str, "utf-8");
			return strencoded;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}

	public Method getMethod() {
		return requestMethod;
	}

	public String getUrl() {
		if (MiscUtils.isEmpty(strUrl)) {
			strUrl = buildRequestUrl(strRequestHost, strRequestCmd, mapRequestParams);
		}
		return strUrl;
	}

	public Integer getTimeout() {
		return nTimeout == null ? DEFAULT_TIMEOUT : nTimeout;
	}

	public void setTimeout(Integer timeout) {
		this.nTimeout = timeout;
	}

	public Map<String, String> getHeaders() {
		return new HashMap<>(mapRequestHeaders);
	}

	public byte[] getBody() {
		return bytesBody;
	}

	public String getBodyToString() {
		return new String(bytesBody);
	}

	public void fillBody(byte[] body) {
		this.bytesBody = body;
	}

	public byte[] buildBody() {
		return bytesBody;
	}

	public String toString() {
		String body = "";
		if (getBody() != null && getBody().length > 0) {
			body = " body: " + new String(getBody());
		}
		String str = "" + getMethod() + " " + getUrl() + body + " header: " + DataConvertUtils.convertMaptoString(getHeaders());
		return str;
	}

	public enum Method {
		DEPRECATED_GET_OR_POST(-1),
		GET(0),
		POST(1),
		PUT(2),
		DELETE(3),
		HEAD(4),
		OPTIONS(5),
		TRACE(6),
		PATCH(7);
		private int mValue;

		Method(int value) {
			mValue = value;
		}

		public int getValue() {
			return mValue;
		}

		@Override
		public String toString() {
			return this.name();
		}
	}
}