package net.ars.sample.actor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class Main {
	private static final int PLAY_TIME_SEC = 60;
	
	public static void main(final String[] args) throws Exception {
		//	System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "error");
		// see: simplelogger.properties
		Config fullConfig = ConfigFactory.load();
		
		Config actorSystemConfig = getActorSystemConfig(args, fullConfig);
		
		ActorSystem actorSystem = ActorSystem.create("ars-akka", actorSystemConfig);
		ActorRef masterRef = actorSystem.actorOf(Props.create(Executive.class), "executive");
		masterRef.tell(Message.OTHER, ActorRef.noSender());
		
		Logger logger = LoggerFactory.getLogger(Main.class);
		// force the printing of this information - hence error
		logger.error("The Main thread going off to sleep for " + PLAY_TIME_SEC + " seconds...");
		
		System.out.println("Press any key to exit...");
		System.in.read();
		
		actorSystem.shutdown();
	}
	
	private static Config getActorSystemConfig(final String[] args, final Config fullConfig) {
		Config result = fullConfig;
		if(args != null && args.length >= 1) {
			result = fullConfig.getConfig(args[0]).withFallback(fullConfig);
		}
		return result;
	}
}
