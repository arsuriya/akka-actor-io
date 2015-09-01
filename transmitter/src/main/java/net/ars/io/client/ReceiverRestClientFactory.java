package net.ars.io.client;

public class ReceiverRestClientFactory {
	
	public static enum Type {
		sync,
		async
	}
	public static ReceiverRestClient getReceiverRestClient(String clientType, String url) {
		// Default implementation is a blocking client
		ReceiverRestClient client = new SyncReceiverRestClient(url);
		if(clientType != null) { 
			if(clientType.equals(Type.async.name())) {
				return new AsyncReceiverRestClient(url);
			}
		}
		return client;
	}
}
