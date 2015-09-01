package net.ars.io.client;

import net.ars.io.Callback;
import net.ars.io.Message;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class SyncReceiverRestClient implements ReceiverRestClient {
	
	private String url;
	private RestTemplate restTemplate;
	public SyncReceiverRestClient(final String url) {
		this.url = url;
		this.restTemplate = new RestTemplate();
	}

	@Override
	public void transmitEvent(Message msg, Callback callback) {
		ResponseEntity<String> response = restTemplate.postForEntity(url, msg.toJson(), String.class);
		callback.onCompletion(response.getStatusCode().value(), response.getBody());
	}

}
