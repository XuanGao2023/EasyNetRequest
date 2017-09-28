package com.pan.commlib.core;

/**
 * Created by pgao on 2017/1/16.
 */
public interface CommListener {
	/**
	 * Got the response successfully.
	 *
	 * @param response available response
	 */
	void onResponse(Response response);

	/**
	 * Can't find the url will trigger onResponseError to be called.
	 *
	 * @param statuscode Http status code
	 * @param response   response.statusCode response.strStatusMsg response.mapHeader is available.
	 */
	void onResponseError(Integer statuscode, Response response);

	/**
	 * Invalid network will trigger onFail function to be called.
	 */
	void onFail();
}
