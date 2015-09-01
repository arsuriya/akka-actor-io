package net.ars.server;

import io.dropwizard.Application;
import io.dropwizard.setup.Environment;
import net.ars.server.resource.OkResource;


public class App extends Application<AppConfiguration> {
    public static void main(final String[] args) throws Exception {
        new App().run(args);
    }

    @Override
    public String getName() {
        return "akka-actor-io-receiver";
    }

	@Override
	public void run(AppConfiguration config, Environment env) throws Exception {
		final OkResource resource = new OkResource(config.getProcessDelayMs());
		env.jersey().register(resource);
	}
}