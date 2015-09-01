package net.ars.io.actor;

import net.ars.io.Message;

public class MonitorManager extends ArsUntypedSupervisorActor {

	@Override
	public void onReceive(final Object msg) throws Exception {
		if(msg instanceof Message) {
			Message message = (Message)msg;
			switch(message.getType()) {
				case MONITORED_EVENT:
					work();
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
		Thread.sleep(10);
		
		//getContext().parent().tell(Message.WORK_FINISHED, getSelf());
	}
	
}
