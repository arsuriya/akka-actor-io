package net.ars.sample.actor;

import akka.actor.Props;

public class Manager extends ArsUntypedSupervisorActor {
	
	public static final int NUM_WORKER = 10;
	
	public Manager() {
		this.maxNumChildren = NUM_WORKER;
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
			createAndWatchChildActor(Props.create(Worker.class), "worker-" + (count+1));
		}
			
		checkMaxChildren(maxNumChildren);
	}

}
