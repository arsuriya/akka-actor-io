package net.ars.io.actor;

import net.ars.io.Message;

import com.codahale.metrics.Counter;
import com.typesafe.config.Config;

public class Tracker extends ArsUntypedActor {

	private Counter processedEventCounter, acknowledgedEventCounter;
	
	public Tracker(Config appConfig, Counter processedEventCounter, Counter acknowledgedEventCounter) {
		this.processedEventCounter = processedEventCounter;
		this.acknowledgedEventCounter = acknowledgedEventCounter;
	}
	
	@Override
	public void onReceive(final Object msg) throws Exception {
		if(msg instanceof Message) {
			Message message = (Message)msg;
			switch(message.getType()) {
				case EVENT_PROCESSED:
					processedEventCounter.inc();
				break;
				
				case EVENT_ACKNOWLEDGED:
					acknowledgedEventCounter.inc();
				break;
				
				default:
					super.handleMessage(message);
			}
		} else {
			super.handleMessage(msg);
		}
	}
	
}
