package net.cltech.enterprisent.domain.operation.audit;

import java.util.Date;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.common.Motive;

/**
 * Representa clase para realizar auditoria a la operación del sistema
 *
 * @version 1.0.0
 * @author eacuna
 * @since 11/10/2017
 * @see Creación
 */
public class AuditEvent
{

    private Motive reason;
    private AuthorizedUser user;
    private String previous;
    private String current;
    private String diferences;
    private Date date;
    private String action;
    private String comment;
    private String type;

    public AuditEvent()
    {
    }

    public AuditEvent(AuthorizedUser user, String previous, String current, String diferences, Date date, String action, String type)
    {
        this.user = user;
        this.previous = previous;
        this.current = current;
        this.diferences = diferences;
        this.date = date;
        this.action = action;
        this.type = type;
    }

    public Motive getReason()
    {
        return reason;
    }

    public void setReason(Motive reason)
    {
        this.reason = reason;
    }

    public AuthorizedUser getUser()
    {
        return user;
    }

    public void setUser(AuthorizedUser user)
    {
        this.user = user;
    }

    public String getPrevious()
    {
        return previous;
    }

    public void setPrevious(String previous)
    {
        this.previous = previous;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getCurrent()
    {
        return current;
    }

    public void setCurrent(String current)
    {
        this.current = current;
    }

    public String getDiferences()
    {
        return diferences;
    }

    public void setDiferences(String diferences)
    {
        this.diferences = diferences;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

    public String getAction()
    {
        return action;
    }

    public void setAction(String action)
    {
        this.action = action;
    }

    public String getComment()
    {
        return comment;
    }

    public void setComment(String comment)
    {
        this.comment = comment;
    }

    @Override
    public int hashCode()
    {
        int hash = 3;
        return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        return true;
    }

}
