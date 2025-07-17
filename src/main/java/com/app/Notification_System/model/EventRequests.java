package com.app.Notification_System.model;

import java.util.Map;
import java.util.Objects;

public class EventRequests 
{
	private EventType eventType;
    private Map<String, Object> payload;
    private String callbackUrl;
    
    public EventRequests() {}
    
	public EventRequests(EventType eventType, Map<String, Object> payload, String callbackUrl) 
	{
		super();
		this.eventType = eventType;
		this.payload = payload;
		this.callbackUrl = callbackUrl;
	}

	public EventType getEventType() 
	{
		return eventType;
	}

	public void setEventType(EventType eventType)
	{
		this.eventType = eventType;
	}

	public Map<String, Object> getPayload() 
	{
		return payload;
	}

	public void setPayload(Map<String, Object> payload) 
	{
		this.payload = payload;
	}

	public String getCallbackUrl() 
	{
		return callbackUrl;
	}

	public void setCallbackUrl(String callbackUrl) 
	{
		this.callbackUrl = callbackUrl;
	}

	@Override
	public int hashCode() 
	{
		return Objects.hash(callbackUrl, eventType, payload);
	}

	@Override
	public boolean equals(Object obj) 
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		EventRequests other = (EventRequests) obj;
		
		return Objects.equals(callbackUrl, other.callbackUrl)
				&& eventType == other.eventType && Objects.equals(payload, other.payload);
	}
}
