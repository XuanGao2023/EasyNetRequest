package com.pan.commlib.request;


import com.google.gson.Gson;
import com.pan.commlib.core.Request;

import java.util.Map;

public class JsonRequest extends Request {

	public JsonRequest(Object payload) {
		super(null);
		mapRequestHeaders.put("Accept", "application/json");
		mapRequestHeaders.put("Content-type", "application/json");
		if (payload == null) {
			fillBody(null);
		} else {
			String json = new Gson().toJson(payload);
			fillBody(json.getBytes());
		}
	}

	public static JsonRequest build(Method method, String host, String command,
									Map<String, String> params, Map<String, String> headers, Object payload) {
		JsonRequest jsonrequest = new JsonRequest(payload);
		jsonrequest.requestMethod = method;
		if (host != null) jsonrequest.strRequestHost = host;
		if (command != null) jsonrequest.strRequestCmd = command;
		if (params != null) jsonrequest.mapRequestParams.putAll(params);
		if (headers != null) jsonrequest.mapRequestHeaders.putAll(headers);
		return jsonrequest;
	}
}
