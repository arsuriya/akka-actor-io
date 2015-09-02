package net.ars.server.resource;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.annotation.Timed;

@Path("/service/ok")
public class OkResource {
	private long processDelayMs;
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
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
    public Response accept(String reqBody) throws Exception {
    	logger.debug("In POST - request body [" + reqBody + "] delay would be [" + processDelayMs + "]");
    	processDelay();
    	return Response.accepted().build();
    }
    
    private void processDelay() throws Exception {
    	Thread.sleep(processDelayMs);
    }
}
