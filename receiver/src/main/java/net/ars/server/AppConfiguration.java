package net.ars.server;

import io.dropwizard.Configuration;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AppConfiguration extends Configuration {
	private long processDelayMs;
	
    @JsonProperty
    public long getProcessDelayMs() {
        return processDelayMs;
    }

    @JsonProperty
    public void setProcessDelayMs(final long processDelayMs) {
        this.processDelayMs = processDelayMs;
    }
}
