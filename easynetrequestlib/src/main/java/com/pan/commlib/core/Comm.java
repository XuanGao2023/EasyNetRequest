package com.pan.commlib.core;

import com.pan.commlib.utils.DataConvertUtils;
import com.pan.commlib.utils.DebugUtils;
import com.pan.commlib.utils.MiscUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by pgao on 2017/1/16.
 */
public class Comm implements Runnable {
	private static final Boolean DEBUG = true;
	Request mRequest;
	Response mResponse;
	WeakReference<CommListener> wrCommListener;

	Comm(Request mRequest, CommListener listener) {
		this.mRequest = mRequest;
		wrCommListener = new WeakReference<CommListener>(listener);
	}

	/**
	 * @param request
	 * @return @Nullable
	 * @throws IOException
	 */
	public static Response sendRequest(Request request) throws IOException {
		if (request == null || MiscUtils.isEmpty(request.getUrl())) {
			throw new IllegalArgumentException("invalid argument! mRequest: " + request +
					" url: " + request.getUrl());
		}
		if (DEBUG) {
			DebugUtils.println("--->>> " + request.toString());
		}
		StringBuffer chaine = new StringBuffer("");
		//1. set url(host + command + "?" + params)
		URL urladdr = new URL(request.getUrl());
		HttpURLConnection connection = (HttpURLConnection) urladdr.openConnection();
		//2. set method
		connection.setRequestMethod(request.getMethod().name());
		//3. set headers
		Iterator itheaders = request.getHeaders().entrySet().iterator();
		while (itheaders.hasNext()) {
			Map.Entry pair = (Map.Entry) itheaders.next();
			connection.setRequestProperty((String) pair.getKey(), (String) pair.getValue());
		}
		//4. set body
		connection.setDoOutput(true);
		byte[] outputInBytes = request.buildBody();
		if (outputInBytes == null) {
			outputInBytes = new byte[0];
		}
		OutputStream outstream = connection.getOutputStream();
		outstream.write(outputInBytes);
		outstream.close();
		//5. set timeout
		connection.setConnectTimeout(request.getTimeout());
		connection.setReadTimeout(request.getTimeout());

		connection.connect();

		int status = connection.getResponseCode();
		Response response = new Response(request);
		//1. fill the statusCode
		response.statusCode = status;
		//2. fill the statusMsg
		response.strStatusMsg = connection.getResponseMessage();
		//4. fill the responseHeader
		Map<String, List<String>> map = connection.getHeaderFields();
		for (Map.Entry<String, List<String>> entry : map.entrySet()) {
			response.mapHeader.put(entry.getKey(),
					DataConvertUtils.convertListToString(entry.getValue()));
		}
		if (status == HttpURLConnection.HTTP_OK) {
			InputStream inputstream = connection.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(inputstream));
			String line = "";
			while ((line = rd.readLine()) != null) {
				chaine.append(line);
			}
			//3. fill the responseBody
			response.strResponseBody = chaine.toString();
		}
		return response;
	}

	public Response send() {
		try {
			mResponse = sendRequest(mRequest);
			if (mResponse != null) {
				if (mResponse.statusCode == HttpURLConnection.HTTP_OK) {
					if (DEBUG)
						DebugUtils.println("<<<--- " + mResponse.toString());
					CommListener commListener = wrCommListener.get();
					if (commListener != null) commListener.onResponse(mResponse);
				} else {
					if (DEBUG)
						DebugUtils.println("<<<--- !!! " + mResponse.statusCode + " message: " + mResponse.strStatusMsg
								+ " header: " + DataConvertUtils.convertMaptoString(mResponse.mapHeader));
					CommListener commListener = wrCommListener.get();
					if (commListener != null)
						commListener.onResponseError(mResponse.statusCode, mResponse);
				}
			} else {
				if (DEBUG) {
					DebugUtils.println("<<<--- !!!!! got null from connection!");
				}
			}
		} catch (java.io.IOException e) {
			//mResponse is null!
			if (DEBUG) {
				e.printStackTrace();
				DebugUtils.println("<<<--- !!!!! Exception got from " + e.getMessage());
			}
			CommListener commListener = wrCommListener.get();
			if (commListener != null) commListener.onFail();
		}
		return mResponse;
	}

	@Override
	public void run() {
		send();
	}
}
