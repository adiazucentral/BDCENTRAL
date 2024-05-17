/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.operation.microbiology;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.common.MasterAudit;
import net.cltech.enterprisent.domain.masters.microbiology.AnatomicalSite;
import net.cltech.enterprisent.domain.masters.microbiology.CollectionMethod;
import net.cltech.enterprisent.domain.masters.microbiology.MediaCulture;
import net.cltech.enterprisent.domain.masters.microbiology.Procedure;
import net.cltech.enterprisent.domain.masters.test.Sample;
import net.cltech.enterprisent.domain.masters.test.TestBasic;
import net.cltech.enterprisent.domain.operation.orders.Order;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa la siembra y verificacion de una muestra en microbiologia
 *
 * @version 1.0.0
 * @author cmartin
 * @since 19/01/2018
 * @see Creación
 */
@ApiObject(
        group = "Operación - Microbiologia",
        name = "Microbiologia - Siembra",
        description = "Representa una siembra de microbiologia de la aplicación"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MicrobiologyGrowth extends MasterAudit
{

    @ApiObjectField(name = "order", description = "Orden", required = false, order = 1)
    private Order order = new Order();
    @ApiObjectField(name = "test", description = "Examen", required = false, order = 2)
    private TestBasic test = new TestBasic();
    @ApiObjectField(name = "test", description = "Examenes asociados a la muestra que manejen medio de cultivo", required = false, order = 3)
    private List<TestBasic> tests;
    @ApiObjectField(name = "sample", description = "Muestra", required = false, order = 3)
    private Sample sample = new Sample();
    @ApiObjectField(name = "subSample", description = "SubMuestra", required = false, order = 4)
    private Sample subSample = new Sample();
    @ApiObjectField(name = "collectionMethod", description = "Metodo de Recolección", required = false, order = 5)
    private CollectionMethod collectionMethod = new CollectionMethod();
    @ApiObjectField(name = "anatomicalSite", description = "Sitio Anatomico", required = false, order = 6)
    private AnatomicalSite anatomicalSite = new AnatomicalSite();
    @ApiObjectField(name = "commentMicrobiology", description = "Comentario de Microbiologia", required = false, order = 7)
    private List<CommentMicrobiology> commentsMicrobiology;
    @ApiObjectField(name = "mediaCultures", description = "Medios de Cultivo", required = false, order = 8)
    private List<MediaCulture> mediaCultures = new ArrayList<>();
    @ApiObjectField(name = "procedures", description = "Procedimientos", required = false, order = 9)
    private List<Procedure> procedures = new ArrayList<>();
    @ApiObjectField(name = "dateGrowth", description = "Fecha de Siembre", required = false, order = 10)
    private Date dateGrowth;
    @ApiObjectField(name = "userGrowth", description = "Usuario que realiza la siembra", required = true, order = 11)
    private AuthorizedUser userGrowth = new AuthorizedUser();
    @ApiObjectField(name = "destination", description = "Destino verificado en microbiologia", required = true, order = 12)
    private int destination;
    

    public Order getOrder()
    {
        return order;
    }

    public void setOrder(Order order)
    {
        this.order = order;
    }

    public TestBasic getTest()
    {
        return test;
    }

    public void setTest(TestBasic test)
    {
        this.test = test;
    }

    public Sample getSample()
    {
        return sample;
    }

    public void setSample(Sample sample)
    {
        this.sample = sample;
    }

    public Sample getSubSample()
    {
        return subSample;
    }

    public void setSubSample(Sample subSample)
    {
        this.subSample = subSample;
    }

    public CollectionMethod getCollectionMethod()
    {
        return collectionMethod;
    }

    public void setCollectionMethod(CollectionMethod collectionMethod)
    {
        this.collectionMethod = collectionMethod;
    }

    public AnatomicalSite getAnatomicalSite()
    {
        return anatomicalSite;
    }

    public void setAnatomicalSite(AnatomicalSite anatomicalSite)
    {
        this.anatomicalSite = anatomicalSite;
    }

    public List<CommentMicrobiology> getCommentsMicrobiology()
    {
        return commentsMicrobiology;
    }

    public void setCommentsMicrobiology(List<CommentMicrobiology> commentsMicrobiology)
    {
        this.commentsMicrobiology = commentsMicrobiology;
    }

    public List<MediaCulture> getMediaCultures()
    {
        return mediaCultures;
    }

    public void setMediaCultures(List<MediaCulture> mediaCultures)
    {
        this.mediaCultures = mediaCultures;
    }

    public List<Procedure> getProcedures()
    {
        return procedures;
    }

    public void setProcedures(List<Procedure> procedures)
    {
        this.procedures = procedures;
    }

    public Date getDateGrowth()
    {
        return dateGrowth;
    }

    public void setDateGrowth(Date dateGrowth)
    {
        this.dateGrowth = dateGrowth;
    }

    public AuthorizedUser getUserGrowth()
    {
        return userGrowth;
    }

    public void setUserGrowth(AuthorizedUser userGrowth)
    {
        this.userGrowth = userGrowth;
    }

    public List<TestBasic> getTests()
    {
        return tests;
    }

    public void setTests(List<TestBasic> tests)
    {
        this.tests = tests;
    }

    public int getDestination() {
        return destination;
    }

    public void setDestination(int destination) {
        this.destination = destination;
    }
    
    

}
