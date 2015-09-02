package net.ars.io.actor;

import java.util.concurrent.TimeUnit;

import net.ars.io.Message;
import akka.actor.ActorRef;

public class Device extends ArsUntypedActor {
	
	private String deviceId;
	private int eventGenerateUs;
	private ActorRef monitor;
	
	public Device(final String deviceId, final int eventGenerateUs, final ActorRef monitor) {
		this.deviceId = deviceId;
		this.eventGenerateUs = eventGenerateUs;
		this.monitor = monitor;
	}
	
	@Override
	public void preStart() throws Exception {
		start();
	}
	
	@Override
	public void onReceive(final Object msg) throws Exception {
		if(msg instanceof Message) {
			Message message = (Message)msg;
			
			debug(message.toString());
			
			switch(message.getType()) {
				case SEND_EVENT:
					long startSendTimeNs = System.nanoTime();
					long endSendTimeNs = System.nanoTime();
					while(endSendTimeNs - startSendTimeNs < 100L) {
						monitor.tell(new Message(Message.Type.MONITORED_EVENT, deviceId), getSelf());
						endSendTimeNs = System.nanoTime();
					}
				break;
				
				default:
					super.handleMessage(message);
			}
		} else {
			super.handleMessage(msg);
		}
	}
		
	private void start() throws Exception {
		scheduleRecurringMessage(eventGenerateUs, TimeUnit.MICROSECONDS, new Message(Message.Type.SEND_EVENT, deviceId), getSelf());
	}

}
