/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import config.TestAppContext;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import net.cltech.outreach.tools.DateTools;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 *
 * @author EAcuna
 * @version 1.0.0
 * @since 19/10/2017
 * @see Creación
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes =
{
    TestAppContext.class
})
@WebAppConfiguration
public class DateToolsTest
{

    @Test
    public void getAgeTest()
    {
        Period period = Period.of(18, 3, 3);
        LocalDate dob = LocalDate.parse("19990716", DateTimeFormatter.ofPattern("yyyyMMdd"));
        LocalDate now = LocalDate.parse("20171019", DateTimeFormatter.ofPattern("yyyyMMdd"));
        Assert.assertEquals(period, DateTools.getAge(dob, now));
    }

    @Test
    public void getAgeInDaysTest()
    {
        LocalDate dob = LocalDate.parse("19990716", DateTimeFormatter.ofPattern("yyyyMMdd"));
        LocalDate now = LocalDate.parse("20171019", DateTimeFormatter.ofPattern("yyyyMMdd"));
        Assert.assertEquals(6670, DateTools.getAgeInDays(dob, now));
    }

}
