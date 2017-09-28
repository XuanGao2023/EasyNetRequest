package com.pan.commlib;


import com.pan.commlib.core.CommListener;
import com.pan.commlib.core.CommManager;
import com.pan.commlib.core.Response;
import com.pan.commlib.request.JsonRequest;
import com.pan.commlib.core.Request;

import junit.framework.Assert;

import org.junit.Test;

public class CommTest {

    boolean called = false;

    @Test(timeout = 5000)
    public void testJsonRequest_onResponse() {
        Request request = JsonRequest.build(Request.Method.GET, "http://init.sms.mob.com/health", null, null, null, null);
        requestTest(request, new CommListener() {
            @Override
            public void onResponse(Response response) {
                called = true;
            }

            @Override
            public void onResponseError(Integer statuscode, Response response) {

            }

            @Override
            public void onFail() {

            }
        });
        Assert.assertTrue(called);
    }

    @Test(timeout = 5000)
    public void testJsonRequest_onResponseError() {
        Request request = JsonRequest.build(Request.Method.GET, "http://init.sms.mob.com/health12", null, null, null, null);
        requestTest(request, new CommListener() {
            @Override
            public void onResponse(Response response) {
            }

            @Override
            public void onResponseError(Integer statuscode, Response response) {
                Assert.assertTrue(statuscode == 404);
                called = true;
            }

            @Override
            public void onFail() {

            }
        });
        Assert.assertTrue(called);
    }

    @Test(timeout = 30000)
    public void testJsonRequest_Timeout() {
        Request request = JsonRequest.build(Request.Method.GET, "http://www.google.com:81", null, null, null, null);
        request.setTimeout(1000);
        requestTest(request, new CommListener() {
            @Override
            public void onResponse(Response response) {

            }

            @Override
            public void onResponseError(Integer statuscode, Response response) {

            }

            @Override
            public void onFail() {

            }
        });
        Assert.assertTrue(called);
    }

    protected void requestTest(Request request, final CommListener listener) {
        Assert.assertNotNull(request);
        final Object objNetworkResponse = new Object();
        CommManager commManager;
        commManager = CommManager.getInstance();

        synchronized (objNetworkResponse) {
            try {
                commManager.sendReqeust(request, new CommListener() {
                    @Override
                    public void onResponse(Response response) {
                        if (listener != null) listener.onResponse(response);
                        notifyWaiting();
                    }

                    @Override
                    public void onResponseError(Integer statuscode, Response response) {
                        if (listener != null) listener.onResponseError(statuscode, response);
                        notifyWaiting();
                    }

                    @Override
                    public void onFail() {
                        if (listener != null) listener.onFail();
                        notifyWaiting();
                    }

                    private void notifyWaiting() {
                        synchronized (objNetworkResponse) {
                            objNetworkResponse.notify();
                        }
                    }
                });
                objNetworkResponse.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
