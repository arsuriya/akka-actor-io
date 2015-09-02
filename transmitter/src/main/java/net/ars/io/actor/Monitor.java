package net.ars.io.actor;

import net.ars.io.Callback;
import net.ars.io.Message;
import net.ars.io.client.ReceiverRestClient;
import akka.actor.ActorSelection;

import com.typesafe.config.Config;

public class Monitor extends ArsUntypedActor {
	
	private ReceiverRestClient receiverRestClient;
	private String trackerPath;
	
	public Monitor(Config appConfig, ReceiverRestClient receiverRestClient, String trackerPath) {
		this.receiverRestClient = receiverRestClient;
		this.trackerPath = trackerPath;
	}

	@Override
	public void onReceive(final Object msg) throws Exception {
		if(msg instanceof Message) {
			Message message = (Message)msg;
			
			debug(msg.toString());
			
			switch(message.getType()) {
				case MONITORED_EVENT:
					work(message);
				break;
				
				default:
					super.handleMessage(message);
			}
		} else {
			super.handleMessage(msg);
		}
	}
	
	private void work(final Message message) throws Exception {
		final ActorSelection tracker = getContext().actorSelection(trackerPath);
		receiverRestClient.transmitEvent(message, new Callback() {
			public void onCompletion(int responseCode, String responseData) {
				tracker.tell(new Message(Message.Type.EVENT_ACKNOWLEDGED, message.getPayload()), getSelf());
			}
		});
		
		tracker.tell(new Message(Message.Type.EVENT_PROCESSED, 1), getSelf());
	}
}
