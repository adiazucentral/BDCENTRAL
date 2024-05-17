package net.cltech.enterprisent.domain.operation.results;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa un examen para el registro de imagenes
 *
 * @version 1.0.0
 * @author oarango
 * @since 04/09/2020
 * @see Creación
 */
@ApiObject(
        group = "Operación - Resultados",
        name = "Imágenes Examen",
        description = "Representa un examen para el registro de resultados"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ImageTest
{

    @ApiObjectField(name = "order", description = "Número de orden", required = true, order = 1)
    private long order;
    @ApiObjectField(name = "testId", description = "Identificador del examen", required = true, order = 2)
    private int testId = 0;
    @ApiObjectField(name = "imageTitle1", description = "Nombre de la imagen 1", required = true, order = 3)
    private String imageName1;
    @ApiObjectField(name = "image1", description = "Primera imagen de resultado de analizador", required = true, order = 4)
    private String image1;
    @ApiObjectField(name = "imageTitle2", description = "Nombre de la imagen 2", required = false, order = 5)
    private String imageName2;
    @ApiObjectField(name = "image2", description = "Segunda imagen de resultado de analizador", required = false, order = 6)
    private String image2;
    @ApiObjectField(name = "imageTitle3", description = "Nombre de la imagen 3", required = false, order = 7)
    private String imageName3;
    @ApiObjectField(name = "image3", description = "Tercera imagen de resultado de analizador", required = false, order = 8)
    private String image3;
    @ApiObjectField(name = "imageTitle4", description = "Nombre de la imagen 4", required = false, order = 9)
    private String imageName4;
    @ApiObjectField(name = "image4", description = "Cuarta imagen de resultado de analizador", required = false, order = 10)
    private String image4;
    @ApiObjectField(name = "imageTitle2", description = "Nombre de la imagen 5", required = false, order = 11)
    private String imageName5;
    @ApiObjectField(name = "image5", description = "Quinta imagen de resultado de analizador", required = false, order = 12)
    private String image5;
    @ApiObjectField(name = "count", description = "Contador de imagenes para la adicion", required = true, order = 13)
    private int count = 0;

    public ImageTest()
    {
    }

    public long getOrder()
    {
        return order;
    }

    public void setOrder(long order)
    {
        this.order = order;
    }

    public int getTestId()
    {
        return testId;
    }

    public void setTestId(int testId)
    {
        this.testId = testId;
    }

    public String getImageName1()
    {
        return imageName1;
    }

    public void setImageName1(String imageName1)
    {
        this.imageName1 = imageName1;
    }

    public String getImage1()
    {
        return image1;
    }

    public void setImage1(String image1)
    {
        this.image1 = image1;
    }

    public String getImageName2()
    {
        return imageName2;
    }

    public void setImageName2(String imageName2)
    {
        this.imageName2 = imageName2;
    }

    public String getImage2()
    {
        return image2;
    }

    public void setImage2(String image2)
    {
        this.image2 = image2;
    }

    public String getImageName3()
    {
        return imageName3;
    }

    public void setImageName3(String imageName3)
    {
        this.imageName3 = imageName3;
    }

    public String getImage3()
    {
        return image3;
    }

    public void setImage3(String image3)
    {
        this.image3 = image3;
    }

    public String getImageName4()
    {
        return imageName4;
    }

    public void setImageName4(String imageName4)
    {
        this.imageName4 = imageName4;
    }

    public String getImage4()
    {
        return image4;
    }

    public void setImage4(String image4)
    {
        this.image4 = image4;
    }

    public String getImageName5()
    {
        return imageName5;
    }

    public void setImageName5(String imageName5)
    {
        this.imageName5 = imageName5;
    }

    public String getImage5()
    {
        return image5;
    }

    public void setImage5(String image5)
    {
        this.image5 = image5;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
    
}
