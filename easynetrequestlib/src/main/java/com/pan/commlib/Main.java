package com.pan.commlib;

import com.pan.commlib.core.CommListener;
import com.pan.commlib.core.CommManager;
import com.pan.commlib.core.Response;
import com.pan.commlib.request.JsonRequest;
import com.pan.commlib.core.Request;

import java.util.concurrent.TimeUnit;

/**
 * Created by pgao on 2017/1/16.
 */
public class Main {

	public static void main(String[] args) {
		CommManager commManager;
		commManager = CommManager.getInstance();
		commManager.sendReqeust(JsonRequest.build(Request.Method.GET, "http://init.sms.mob.com/health", null, null, null, null), new CommListener() {
			@Override
			public void onResponse(Response response) {
				System.out.println("response: " + response.strResponseBody);
			}

			@Override
			public void onResponseError(Integer statuscode, Response response) {
				System.out.println("onResponseError: " + response.strResponseBody);

			}

			@Override
			public void onFail() {
				System.out.println("onFail");

			}
		});

		try {
			TimeUnit.SECONDS.sleep(5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
