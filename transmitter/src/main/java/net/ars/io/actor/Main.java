package net.ars.io.actor;

import java.util.concurrent.TimeUnit;

import org.slf4j.LoggerFactory;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Slf4jReporter;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class Main {
	private static final MetricRegistry registry = new MetricRegistry();
	
	public static void main(final String[] args) throws Exception {
		//	System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "error");
		// see: simplelogger.properties
		Config fullConfig = ConfigFactory.load();
		Config actorSystemConfig = getActorSystemConfig(args, fullConfig);
		Config appConfig = getAppConfig(fullConfig);
		
		//final ConsoleReporter reporter = ConsoleReporter.forRegistry(registry)
		final Slf4jReporter reporter = Slf4jReporter.forRegistry(registry)
                .outputTo(LoggerFactory.getLogger("net.ars.metrics"))
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .build();
                
		reporter.start(1, TimeUnit.SECONDS);
		
		Counter processedEventCounter = registry.counter("processed-events-count");
		
		ActorSystem actorSystem = ActorSystem.create("ars-akka", actorSystemConfig);
		ActorRef trackerRef = actorSystem.actorOf(Props.create(Tracker.class, appConfig), "tracker");
		ActorRef monitorRef = actorSystem.actorOf(Props.create(Monitor.class, appConfig), "monitor");
		ActorRef mainRef = actorSystem.actorOf(Props.create(NoE.class, appConfig, processedEventCounter), "main-mgr");
		
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
	
	private static Config getAppConfig(final Config fullConfig) {
		return fullConfig.getConfig("app").withFallback(fullConfig);
	}
}
