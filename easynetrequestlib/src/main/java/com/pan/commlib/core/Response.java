package com.pan.commlib.core;


import com.pan.commlib.utils.DataConvertUtils;

import java.util.HashMap;
import java.util.Map;

public class Response {
	private Request mRequest;

	public Integer statusCode;
	public String strStatusMsg;
	public Map<String, String> mapHeader = new HashMap<>();
	public String strResponseBody;

	public Request getRequest() {
		return mRequest;
	}

	Response(Request request) {
		this.mRequest = request;
	}

	@Override
	public String toString() {
		String requeststr = "";
		String str = (statusCode != null ? ("" + statusCode) : "")
				+ (mRequest != null ? (" " + mRequest.getMethod() + " " + mRequest.getUrl()) : "")
				+ (strResponseBody != null ? (" response: " + strResponseBody) : "")
				+ (mapHeader != null ? (" header: " + DataConvertUtils.convertMaptoString(mapHeader)) : "");
		return str;
	}
}
