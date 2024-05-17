/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.operation.audit;

import java.util.Date;
import net.cltech.enterprisent.domain.masters.common.Motive;
import net.cltech.enterprisent.domain.masters.user.User;

/**
 *
 * @author EAcuna
 * @version 1.0.0
 * @since 30/10/2017
 * @see Creación
 */
public class Action
{

    private Motive reason;
    private User user;
    private String action;
    private String json;
    private Date date;

    public Motive getReason()
    {
        return reason;
    }

    public void setReason(Motive reason)
    {
        this.reason = reason;
    }

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    public String getAction()
    {
        return action;
    }

    public void setAction(String action)
    {
        this.action = action;
    }

    public String getJson()
    {
        return json;
    }

    public void setJson(String json)
    {
        this.json = json;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

}
