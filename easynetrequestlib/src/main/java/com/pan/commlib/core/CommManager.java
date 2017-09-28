package com.pan.commlib.core;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by pgao on 2017/1/16.
 */
public class CommManager {
	private static final String TAG = "CommManager";

	private static CommManager instance;
	private ExecutorService executorService = Executors.newFixedThreadPool(10);

	private CommManager() {
	}

	public synchronized static CommManager getInstance() {
		if (instance == null) instance = new CommManager();
		return instance;
	}

	public void sendReqeust(final Request request, CommListener listener) {
		executorService.submit(new Comm(request, listener));
	}
}
