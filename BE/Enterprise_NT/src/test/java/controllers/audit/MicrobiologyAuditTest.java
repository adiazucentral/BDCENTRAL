package controllers.audit;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flipkart.zjsonpatch.JsonDiff;
import config.MongoTestAppContext;
import config.TestAppContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.microbiology.AnatomicalSite;
import net.cltech.enterprisent.domain.masters.microbiology.MediaCulture;
import net.cltech.enterprisent.domain.masters.microbiology.Procedure;
import net.cltech.enterprisent.domain.masters.test.TestBasic;
import net.cltech.enterprisent.domain.operation.microbiology.CommentMicrobiology;
import net.cltech.enterprisent.domain.operation.microbiology.MicrobiologyGrowth;
import net.cltech.enterprisent.tools.Tools;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 *
 * @author eacuña
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes =
{
    MongoTestAppContext.class,
    TestAppContext.class
})
@WebAppConfiguration
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MicrobiologyAuditTest
{

    @Test
    public void test_01_getAllCardNoContent() throws Exception
    {
        Date verifyDate = new Date();
        MicrobiologyGrowth insert = new MicrobiologyGrowth();
        insert.setAnatomicalSite(new AnatomicalSite());
        insert.getAnatomicalSite().setId(1);
        CommentMicrobiology coment = new CommentMicrobiology();
        coment.setComment("Comentario de inserción");
        insert.setCommentsMicrobiology(Arrays.asList(coment));
        insert.setTest(new TestBasic(1));
        insert.setUserGrowth(new AuthorizedUser(2));
        insert.setLastTransaction(verifyDate);

        MicrobiologyGrowth mod = new MicrobiologyGrowth();
        mod.setAnatomicalSite(null);
        coment = new CommentMicrobiology();
        coment.setComment("Comentario de microbiologia");
        mod.setCommentsMicrobiology(Arrays.asList(coment));
        mod.setTest(new TestBasic(1));
        mod.setUserGrowth(new AuthorizedUser(2));
        mod.setLastTransaction(verifyDate);
        mod.setMediaCultures(new ArrayList<>());
        mod.getMediaCultures().add(new MediaCulture());
        mod.getMediaCultures().add(new MediaCulture());
        mod.getMediaCultures().get(0).setId(1);
        mod.getMediaCultures().get(1).setId(2);

        mod.setProcedures(new ArrayList<>());
        mod.getProcedures().add(new Procedure());
        mod.getProcedures().get(0).setId(1);
        mod.getProcedures().get(0).setName("Pr1");

        ObjectMapper jackson = new ObjectMapper();
        long t1 = System.currentTimeMillis();
        JsonNode diff = JsonDiff.asJson(jackson.readTree(Tools.jsonObject(insert)), jackson.readTree(Tools.jsonObject(mod)));
        long t2 = System.currentTimeMillis();
        System.out.println("Comparando" + (t2 - t1));
        System.out.println(diff.toString());
    }
}
