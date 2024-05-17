/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.barcode;

import config.MongoTestAppContext;
import config.TestAppContext;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import net.cltech.enterprisent.domain.operation.orders.barcode.BarcodeOrder;
import net.cltech.enterprisent.tools.Constants;
import net.cltech.enterprisent.tools.barcode.BarcodeTools;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 *
 * @author EAcuna
 * @version 1.0.0
 * @since 26/09/2017
 * @see Creación
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes =
{
    MongoTestAppContext.class,
    TestAppContext.class
})
@WebAppConfiguration
public class BarcodeTest
{

    @Test
    public void sampleBarcodeTest() throws MalformedURLException, ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException
    {

        BarcodeOrder info = new BarcodeOrder();

        info.getDemos().put("" + Constants.PATIENT_LAST_NAME, "ACUÑA");
        info.getDemos().put("" + Constants.PATIENT_SURNAME, "RUBIO");
        info.getDemos().put("" + Constants.PATIENT_NAME, "NAME");
        info.getDemos().put("" + Constants.PATIENT_SEX, "1.MASCULINO");
        info.getDemos().put("CN", "1234654");
        info.getDemos().put("" + Constants.PATIENT_BIRTHDAY, "2017-09-26");
        info.getDemos().put("" + Constants.ORDERTYPE, "R.RUTINA");

        info.setOrder("201709260001");
        info.setPrintSample(true);
        info.setConfiguration("1,2,3");
        info.setSeparator(".");
        info.setYearDigits(4);
        info.setPrintNumberSample(1);
        info.setSampleCode("03");
        info.setSampleName("SUERO");
        info.setSampleRecipient("TAPA LILA");
        String folder = System.getProperty("user.dir") + System.getProperty("file.separator") + "/src/main/webapp/barcode/";
        System.out.println("Dir" + folder);
        String command = BarcodeTools.sampleBarcode(folder + "PrinterService-1.0.0.jar", info);
        assert !command.isEmpty();

    }
}
