package net.ars.sample.actor;

import com.typesafe.config.Config;

public class Tracker extends ArsUntypedActor {

	public final int THRESHOLD;
	private int processedEventCount = 0;
	
	public Tracker(Config appConfig) {
		int t = appConfig.getInt("status-update-count");
		if(t > 0) {
			THRESHOLD = t;
		} else {
			THRESHOLD = 1000;
		}
		logger.debug("Status update threshold is : " + THRESHOLD);
	}
	
	@Override
	public void onReceive(final Object msg) throws Exception {
		if(msg instanceof Message) {
			Message message = (Message)msg;
			switch(message.getType()) {
				case EVENT_PROCESSED:
					processedEventCount++;
					if(processedEventCount == THRESHOLD) {
						informNoe();
						processedEventCount = 0;
					}
				break;
				
				default:
					super.handleMessage(message);
			}
		} else {
			super.handleMessage(msg);
		}
	}
	
	private void informNoe() throws Exception {
		getContext().actorSelection("../main-mgr").tell(new Message(Message.Type.PROCESSED_EVENT_COUNT, THRESHOLD), getSelf());
	}
	
}
