package net.ars.sample.actor;


public class Message {
	static enum Type {
		HEALTH_CHECK,
		SEND_EVENT,
		MONITORED_EVENT,
		EVENT_PROCESSED,
		EVENT_ACKNOWLEDGED,
		PROCESSED_EVENT_COUNT,
		OTHER
	}
	
	public static final Message HealthCheckMessage = new Message(Type.HEALTH_CHECK, null);
	
	private final Type type;
	// private final Map<String, String> attributes;
	private final Object payload;
	
	public Message(Type messageType, Object payload) {
		this.type = messageType;
		// this.attributes = attrs;
		this.payload = payload;
	}
	
	public Type getType() {
		return type;
	}
	/*
	public Map<String, String> getAttributes() {
		return Collections.unmodifiableMap(attributes);
	}
	*/
	public Object getPayload() {
		return payload;
	}
	
	public String toString() {
		return "Message : ["
				+ "type: " + this.getType()
				+ "payload: " + this.getPayload()
				+ "]";
	}
}
