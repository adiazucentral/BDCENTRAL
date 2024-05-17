/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.common.Item;
import net.cltech.enterprisent.domain.masters.configuration.Configuration;
import net.cltech.enterprisent.domain.masters.demographic.Branch;
import net.cltech.enterprisent.domain.masters.demographic.DocumentType;
import net.cltech.enterprisent.domain.masters.demographic.OrderType;
import net.cltech.enterprisent.domain.masters.user.RoleByUser;
import net.cltech.enterprisent.domain.masters.user.User;
import net.cltech.enterprisent.domain.operation.orders.Order;
import net.cltech.enterprisent.domain.operation.orders.Patient;
import net.cltech.enterprisent.domain.operation.orders.Test;
import net.cltech.enterprisent.tools.DateTools;
import net.cltech.enterprisent.tools.JWT;
import net.cltech.enterprisent.tools.Tools;
import net.cltech.enterprisent.tools.enums.ListEnum;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * Clases de utilidad para pruebas
 *
 * @version 1.0.0
 * @author dcortes
 * @since 18/04/2017
 * @see Creacion
 */
public class TestTools
{

    /**
     * Representa el tipo de dato json en codificacion utf-8
     */
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
    
    public static String getResponseString(ResultActions request) throws UnsupportedEncodingException
    {
        return request.andReturn().getResponse().getContentAsString();
    }
    
    public static Branch firstBranch(MockMvc mockMvc, String token) throws Exception
    {
        List<Branch> branchList = Tools.jsonList(TestTools.getResponseString(mockMvc.perform(get("/api/branches")
                .header("Authorization", token))), Branch.class);
        if (branchList.isEmpty())
        {
            Branch newBranch = new Branch();
            newBranch.setAbbreviation("GR");
            newBranch.setCode("01");
            newBranch.setName("General");
            newBranch.setMinimum(1);
            newBranch.setMaximum(9999);
            String response = TestTools.getResponseString(mockMvc.perform(post("/api/branches")
                    .header("Authorization", token)
                    .contentType(TestTools.APPLICATION_JSON_UTF8)
                    .content(Tools.jsonObject(newBranch))));
            
            return Tools.jsonObject(response, Branch.class);
        } else
        {
            return branchList.get(0);
        }
    }
    
    public static String userToken(MockMvc mockMvc, String token) throws Exception
    {
        User testUser = userTest(mockMvc, token);
        AuthorizedUser user = new AuthorizedUser();
        user.setUserName(testUser.getUserName());
        user.setLastName(testUser.getLastName());
        user.setName(testUser.getName());
        user.setId(testUser.getId());
        user.setBranch(firstBranch(mockMvc, token).getId());
        return JWT.generate(user, Integer.parseInt(Tools.jsonObject(TestTools.getResponseString(mockMvc.perform(get("/api/configuration/tokenexpiration"))), Configuration.class).getValue()));
    }
    
    public static int idUserToken(MockMvc mockMvc, String token) throws Exception
    {
        User testUser = userTest(mockMvc, token);
        return testUser.getId();
    }
    
    public static User userTest(MockMvc mockMvc, String token) throws Exception
    {
        User testUser = Tools.jsonObject(TestTools.getResponseString(mockMvc.perform(get("/api/users/filter/username/tests")
                .header("Authorization", token))), User.class);
        if (testUser == null)
        {
            Timestamp timestamp = new Timestamp(new Date().getTime());
            Timestamp timestampExp = new Timestamp(new Date().getTime() + 132512652);
            User user = new User();
            user.setName("CLTech");
            user.setLastName("Pruebas");
            user.setUserName("tests");
            user.setPassword("12345");
            user.setActivation(timestamp);
            user.setExpiration(timestampExp);
            user.getUser().setId(1);
            user.setIdentification("10071001081");
            user.setEmail("a2@b.c");
            user.setSignatureCode("123456");
            user.setMaxDiscount(0.0);
            user.getType().setId(11);
            user.setConfidential(true);
            user.setPrintInReports(true);
            user.setAddExams(true);
            user.setSecondValidation(true);
            user.setEditPatients(true);
            user.setQuitValidation(true);
            user.setCreatingItems(true);
            user.setPrintResults(true);
            
            RoleByUser role = new RoleByUser();
            role.getRole().setId(1);
            role.setAccess(true);
            user.getRoles().add(role);
            
            String response = TestTools.getResponseString(mockMvc.perform(post("/api/users")
                    .header("Authorization", token)
                    .contentType(TestTools.APPLICATION_JSON_UTF8)
                    .content(Tools.jsonObject(user))));
            
            return Tools.jsonObject(response, User.class);
        } else
        {
            return testUser;
        }
    }
    
    public static Order createNewOrder(MockMvc mockMvc, String token, List<Test> tests) throws Exception
    {
        Order order = new Order();
        order.setType(new OrderType(1));//Rutina
        order.getType().setCode("R");//No controlado en servicio
        order.setBranch(firstBranch(mockMvc, token));
        order.setCreatedDateShort(DateTools.dateToNumber(new Date()));
        //Paciente
        order.setPatient(new Patient());
        order.getPatient().setPatientId("80189895");
        order.getPatient().setDocumentType(new DocumentType());
        order.getPatient().getDocumentType().setId(1);
        order.getPatient().setName1("Edwin");
        order.getPatient().setName2("de Jesus");
        order.getPatient().setLastName("Acuña Rubio");
        order.getPatient().setSex(new Item(ListEnum.Gender.MALE.getValue()));
        order.getPatient().setBirthday(DateTools.asDate(LocalDate.of(1984, 4, 11)));
        //Examenes
        order.setTests(tests);
        
        String response = TestTools.getResponseString(mockMvc.perform(post("/api/orders")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(Tools.jsonObject(order))));
        return Tools.jsonObject(response, Order.class);
    }
    
}
