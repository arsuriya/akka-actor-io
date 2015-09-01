package net.ars.io.actor;

import net.ars.io.Callback;
import net.ars.io.Message;
import net.ars.io.client.ReceiverRestClient;
import akka.actor.ActorRef;

import com.typesafe.config.Config;

public class Monitor extends ArsUntypedActor {
	private final int maxEventsToProcess;
	private int eventCount = 0;
	private long startNs, endNs;
	private ReceiverRestClient receiverRestClient;
	
	public Monitor(Config appConfig, ReceiverRestClient receiverRestClient) {
		this.maxEventsToProcess = appConfig.getInt("max-events");
		this.receiverRestClient = receiverRestClient;
	}
	
	public void preStart() {
		startNs = System.nanoTime();
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
				
				case EVENT_ACKNOWLEDGED:
					updateCount();
				break;
				
				default:
					super.handleMessage(message);
			}
		} else {
			super.handleMessage(msg);
		}
	}
	
	private void work(final Message message) throws Exception {
		final ActorRef monitorRef = getSelf();
		receiverRestClient.transmitEvent(message, new Callback() {
			public void onCompletion(int responseCode, String responseData) {
				monitorRef.tell(new Message(Message.Type.EVENT_ACKNOWLEDGED, message.getPayload()), getSelf());
			}
		});
		
		// complete work
		getContext().actorSelection("../tracker").tell(new Message(Message.Type.EVENT_PROCESSED, 1), getSelf());
	}
	
	private void updateCount() throws Exception {
		eventCount++;
		if(eventCount == maxEventsToProcess) {
			endNs = System.nanoTime();
			info(maxEventsToProcess + " events acknowledged in " + (endNs - startNs) + " ns");
		}
	}

}
