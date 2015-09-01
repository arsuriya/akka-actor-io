package net.ars.io.client;

import net.ars.io.Callback;
import net.ars.io.Message;

public interface ReceiverRestClient {
	public void transmitEvent(Message msg, Callback callback);
}
