package net.ars.io.client;

import net.ars.io.Callback;
import net.ars.io.Message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.client.AsyncRestTemplate;

public class AsyncReceiverRestClient implements ReceiverRestClient {

	private String url;
	private AsyncRestTemplate asyncRestTemplate;
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public AsyncReceiverRestClient(final String url) {
		this.url = url;
		this.asyncRestTemplate = new AsyncRestTemplate();
	}
	
	@Override
	public void transmitEvent(final Message msg, final Callback callback) {
		ListenableFuture<ResponseEntity<String>> futureResponse = asyncRestTemplate.postForEntity(url, new HttpEntity<String>(msg.toJson()), String.class);
		futureResponse.addCallback(new ListenableFutureCallback<ResponseEntity<String>>() {

			@Override
			public void onSuccess(ResponseEntity<String> response) {
				callback.onCompletion(response.getStatusCode().value(), response.getBody());
			}

			@Override
			public void onFailure(Throwable ex) {
				logger.error("not concerned about failures much...", ex);
			}
		});
	}

}
