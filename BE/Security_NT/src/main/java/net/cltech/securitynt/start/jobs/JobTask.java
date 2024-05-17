/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.securitynt.start.jobs;

import java.util.Calendar;

public class JobTask
{

    public void expiredSample() throws Exception
    {
        System.out.println("Current Time job 1 : " + Calendar.getInstance().getTime());
    }

    public void newYear() throws Exception
    {
        System.out.println("Current NEW YEAR : " + Calendar.getInstance().getTime());
    }
}
