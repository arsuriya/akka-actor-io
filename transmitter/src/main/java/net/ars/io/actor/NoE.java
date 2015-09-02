package net.ars.io.actor;

import scala.concurrent.Await;
import scala.concurrent.Future;
import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.Props;
import akka.util.Timeout;

import com.typesafe.config.Config;

public class NoE extends ArsUntypedSupervisorActor {

	private final int deviceEventGenerateDuration;
	
	public NoE(final Config appConfig) {
		this.maxNumChildren = appConfig.getInt("num-devices");
		this.deviceEventGenerateDuration = appConfig.getInt("device-event-duration-us");
	}
	
	@Override
	public void preStart() throws Exception {
		start();
	}
	
	@Override
	public void onReceive(final Object msg) throws Exception {
		super.handleMessage(msg);
	}
	
	private void start() throws Exception {
		ActorSelection as = getContext().actorSelection("../monitor");
		Future<ActorRef> monitorF = as.resolveOne(Timeout.apply(10));
		
		ActorRef monitor = Await.result(monitorF, Timeout.apply(100).duration());
		
		for(int count = 0; count < maxNumChildren; count++) {
			String deviceId = "device-" + (count+1);
			createAndWatchChildActor(Props.create(Device.class, deviceId, 
					deviceEventGenerateDuration, monitor), deviceId);
		}
	}

}
