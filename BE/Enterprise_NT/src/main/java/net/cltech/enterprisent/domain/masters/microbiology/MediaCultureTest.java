package net.cltech.enterprisent.domain.masters.microbiology;

import java.util.ArrayList;
import java.util.List;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa la relacion de prueba y medio de cultivo
 *
 * @version 1.0.0
 * @author enavas
 * @since 14/08/2017
 * @see Creacion
 */
@ApiObject(
        group = "Prueba",
        name = "Prueba por medio de cultivo",
        description = "Representa la relacion de prueba por medio de cultivo"
)
public class MediaCultureTest
{

    @ApiObjectField(name = "testId", description = "Id Examen", required = true, order = 1)
    private Integer testId;
    @ApiObjectField(name = "mediaCulture", description = "Medio de cultivo", required = true, order = 2)
    private List<MediaCulture> mediaCultures;
    @ApiObjectField(name = "testName", description = "Nombre del examen Examen", required = false, order = 1)
    private String testName;

    public MediaCultureTest()
    {
        mediaCultures = new ArrayList<>(0);
    }

    public Integer getTestId()
    {
        return testId;
    }

    public void setTestId(Integer testId)
    {
        this.testId = testId;
    }

    public List<MediaCulture> getMediaCultures()
    {
        return mediaCultures;
    }

    public void setMediaCultures(List<MediaCulture> mediaCultures)
    {
        this.mediaCultures = mediaCultures;
    }

    public String getTestName()
    {
        return testName;
    }

    public void setTestName(String testName)
    {
        this.testName = testName;
    }

}
