package net.ars.io.actor;

import com.typesafe.config.Config;

public class Monitor extends ArsUntypedActor {
	private final int maxEventsToProcess;
	private int eventCount = 0;
	private long startNs, endNs;
	
	public Monitor(Config appConfig) {
		this.maxEventsToProcess = appConfig.getInt("max-events");
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
					work();
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
	
	private void work() throws Exception {
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
