package com.pan.commapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.pan.commlib.core.CommListener;
import com.pan.commlib.core.CommManager;
import com.pan.commlib.core.Response;
import com.pan.commlib.request.JsonRequest;
import com.pan.commlib.core.Request;

/**
 * Created by pgao on 2017/9/25.
 */

public class TestActivity extends AppCompatActivity {

	private static final String TAG = "TestActivity";

	CommListener listener = new CommListener() {
		@Override
		public void onResponse(Response response) {
			Log.d(TAG, "onResponse: " + response.strResponseBody);
		}

		@Override
		public void onResponseError(Integer statuscode, Response response) {
			Log.d(TAG, "statuscode: " + statuscode + " response: " + response.strStatusMsg);
		}

		@Override
		public void onFail() {
			Log.d(TAG, "onFail()");
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);

		findViewById(R.id.btnSendRequest).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d(TAG, "btnSendRequest onclick");
				Request request = JsonRequest.build(Request.Method.GET, "http://www.google.com:81", null, null, null, null);
				CommManager commManager;
				commManager = CommManager.getInstance();
				commManager.sendReqeust(request, listener);

				request = JsonRequest.build(Request.Method.GET, "http://init.sms.mob.com/health", null, null, null, null);
				commManager.sendReqeust(request, listener);

				request = JsonRequest.build(Request.Method.GET, "http://asfdazc.com/health", null, null, null, null);
				commManager.sendReqeust(request, listener);

				byte[] bytes = {69, 121, 101, 45, 62, 118, 101, 114, (byte) 196, (byte) 195, 61, 101, 98};
				request = new Request(bytes) {
					@Override
					public void init() {
						super.init();
						requestMethod = Method.GET;
						strRequestHost = "http://init.sms.mob.com/health";
					}
				};
				commManager.sendReqeust(request, listener);
			}
		});
	}
}
