
package net.cltech.enterprisent.domain.operation.audit;

import java.util.List;

/**
 *
 * @author eacuna
 */
public class TrackInformation
{
    private String order;
    private String test;
    private List<AuditEvent> events;

    public TrackInformation(String order)
    {
        this.order = order;
    }

    public TrackInformation(String order, String test)
    {
        this.order = order;
        this.test = test;
    }

    public String getOrder()
    {
        return order;
    }

    public void setOrder(String order)
    {
        this.order = order;
    }

    public String getTest()
    {
        return test;
    }

    public void setTest(String test)
    {
        this.test = test;
    }

    public List<AuditEvent> getEvents()
    {
        return events;
    }

    public void setEvents(List<AuditEvent> events)
    {
        this.events = events;
    }
    
    
    
}
