package net.ars.sample.actor;

import akka.actor.Props;

public class Executive extends ArsUntypedSupervisorActor {
	
	private static final int NUM_MANAGER = 1;
	
	public Executive() {
		this.maxNumChildren = NUM_MANAGER;
	}
	
	@Override
	public void preStart() {
		start();
	}
	
	@Override
	public void onReceive(final Object msg) throws Exception {
		if(msg instanceof Message) {
			Message message = (Message)msg;
			switch(message) {
				default:
					super.handleMessage(message);
			}
		} else {
			super.handleMessage(msg);
		}
	}
	
	private void start() {
		for(int count = 0; count < maxNumChildren; count++) {
			createAndWatchChildActor(Props.create(Manager.class), "manager-" + (count+1));
		}
		
		// scheduleHealthCheck();
		
		checkMaxChildren(maxNumChildren);
	}
}
