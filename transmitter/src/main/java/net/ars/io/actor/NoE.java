package net.ars.io.actor;

import scala.concurrent.Await;
import scala.concurrent.Future;
import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.Props;
import akka.util.Timeout;

import com.codahale.metrics.Counter;
import com.typesafe.config.Config;

public class NoE extends ArsUntypedSupervisorActor {

	private final int deviceEventGenerateDuration;
	private final int maxEventsToProcess;
	
	private long startNs, endNs;
	private long eventCount = 0;
	private Counter processedEventCounter;
	
	public NoE(final Config appConfig, Counter processedEventCounter) {
		this.maxNumChildren = appConfig.getInt("num-devices");
		this.maxEventsToProcess = appConfig.getInt("max-events");
		this.deviceEventGenerateDuration = appConfig.getInt("device-event-duration-us");
		this.processedEventCounter = processedEventCounter;
	}
	
	@Override
	public void preStart() throws Exception {
		startNs = System.nanoTime();
		start();
	}
	
	@Override
	public void onReceive(final Object msg) throws Exception {
		if(msg instanceof Message) {
			Message message = (Message)msg;
			
			debug(message.toString());
			
			switch(message.getType()) {
				case PROCESSED_EVENT_COUNT:
					int cnt = (int)message.getPayload();
					eventCount += (long)cnt;
					if(eventCount >= maxEventsToProcess) {
						endNs = System.nanoTime();
						info(maxEventsToProcess + " events sent in " + (endNs - startNs) + " ns");
						eventCount=0;
					}
					processedEventCounter.inc((long)cnt);
				break;
				
				default:
					super.handleMessage(message);
			}
		} else {
			super.handleMessage(msg);
		}
	}
	
	private void start() throws Exception {
		ActorSelection as = getContext().actorSelection("../monitor");
		Future<ActorRef> monitorF = as.resolveOne(Timeout.apply(10));
		
		ActorRef monitor = Await.result(monitorF, Timeout.apply(100).duration());
		
		for(int count = 0; count < maxNumChildren; count++) {
			String deviceId = "device-" + (count+1);
			createAndWatchChildActor(Props.create(Device.class, deviceId, deviceEventGenerateDuration, monitor), deviceId);
		}
	}

}
