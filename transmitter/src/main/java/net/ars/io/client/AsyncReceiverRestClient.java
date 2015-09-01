package net.ars.io.client;

import net.ars.io.Callback;
import net.ars.io.Message;

public class AsyncReceiverRestClient implements ReceiverRestClient {

	private String url;
	public AsyncReceiverRestClient(final String url) {
		this.url = url;
	}
	
	@Override
	public void transmitEvent(Message msg, Callback callback) {
	}

}
