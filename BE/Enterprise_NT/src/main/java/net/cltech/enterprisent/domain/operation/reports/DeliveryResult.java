package net.cltech.enterprisent.domain.operation.reports;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.operation.orders.TestList;
import net.cltech.enterprisent.domain.operation.results.ResultTest;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa clase con filtros para busquedas por demografico
 *
 * @version 1.0.0
 * @author cmartin
 * @since 02/10/2017
 * @see Creación
 */
@ApiObject(
        group = "Operación - Informes",
        name = "Entrega de Resultados",
        description = "Representa filtro con parametros para busquedas por demografico."
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeliveryResult
{

    @ApiObjectField(name = "id", description = "Id del control de entrega", order = 1)
    private Integer id;
    @ApiObjectField(name = "orders", description = "Lista de Ordenes", order = 2)
    private List<Long> orders;
    @ApiObjectField(name = "orderNumber", description = "Orden", order = 3)
    private Long orderNumber;
    @ApiObjectField(name = "user", description = "Usuario", order = 4)
    private AuthorizedUser user = new AuthorizedUser();
    @ApiObjectField(name = "receivesPerson", description = "Persona que Recibe", order = 5)
    private String receivesPerson;
    @ApiObjectField(name = "date", description = "Fecha de Entrega", order = 6)
    private Date date;
    
    @ApiObjectField(name = "idTest", description = "Id de examen", order = 7)
    private Integer idTest;
    @ApiObjectField(name = "idProfile", description = "Id del perfil", order = 8)
    private Integer idProfile;
    @ApiObjectField(name = "typeDelivery", description = "medio de entrega", order = 9)
    private Integer typeDelivery;
   
    @ApiObjectField(name = "resultTest", description = "Examenes", order = 7)
    private List<ResultTest> resultTest = new ArrayList<>();
    @ApiObjectField(name = "tests", description = "Examenes", order = 8)
    private List<TestList> tests = new ArrayList<>();

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public List<Long> getOrders()
    {
        return orders;
    }

    public void setOrders(List<Long> orders)
    {
        this.orders = orders;
    }

    public Long getOrderNumber()
    {
        return orderNumber;
    }

    public void setOrderNumber(Long orderNumber)
    {
        this.orderNumber = orderNumber;
    }

    public AuthorizedUser getUser()
    {
        return user;
    }

    public void setUser(AuthorizedUser user)
    {
        this.user = user;
    }

    public String getReceivesPerson()
    {
        return receivesPerson;
    }

    public void setReceivesPerson(String receivesPerson)
    {
        this.receivesPerson = receivesPerson;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

    public List<ResultTest> getResultTest()
    {
        return resultTest;
    }

    public void setResultTest(List<ResultTest> resultTest)
    {
        this.resultTest = resultTest;
    }

    public List<TestList> getTests() {
        return tests;
    }

    public void setTests(List<TestList> tests) {
        this.tests = tests;
    }

    public Integer getIdTest() {
        return idTest;
    }

    public void setIdTest(Integer idTest) {
        this.idTest = idTest;
    }

    public Integer getIdProfile() {
        return idProfile;
    }

    public void setIdProfile(Integer idProfile) {
        this.idProfile = idProfile;
    }

    public Integer getTypeDelivery() {
        return typeDelivery;
    }

    public void setTypeDelivery(Integer typeDelivery) {
        this.typeDelivery = typeDelivery;
    }
    
    
}
