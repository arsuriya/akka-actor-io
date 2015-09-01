package net.ars.server.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import com.codahale.metrics.annotation.Timed;

@Path("/service/ok")
@Consumes(MediaType.APPLICATION_JSON)
public class OkResource {
	private long processDelayMs;
	
	public OkResource(final long processDelay) {
        this.processDelayMs = processDelay;
    }

    @GET
    @Timed
    public String howdy() throws Exception {
    	processDelay();
    	return "Ok";
    }
    
    @POST
    @Timed
    public String accept() throws Exception {
    	processDelay();
    	return "Ok - " + System.nanoTime();
    }
    
    private void processDelay() throws Exception {
    	Thread.sleep(processDelayMs);
    }
}
