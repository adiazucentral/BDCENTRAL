package net.cltech.enterprisent.controllers.doc;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.jsondoc.core.pojo.JSONDoc;
import org.jsondoc.core.scanner.DefaultJSONDocScanner;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controlador sobre la pagina de documentacion
 *
 * @version 1.0.0
 * @author dcortes
 * @since 09/09/2016
 * @see Creacion
 */
@Controller
public class JsonDocController
{

    @RequestMapping(value = "/jsondoc", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public JSONDoc getJsonDoc(HttpServletRequest request)
    {
        List<String> packages = new ArrayList<>();
        packages.add("net.cltech.enterprisent.controllers");
        packages.add("net.cltech.enterprisent.domain");
        return new DefaultJSONDocScanner().getJSONDoc(
                "1.0.0",
                request.getRequestURL().toString().split("/jsondoc")[0],
                packages,
                true,
                JSONDoc.MethodDisplay.URI
        );
    }

    @RequestMapping(value = "/jsondocui", method = RequestMethod.GET)
    public String getJsonDocUI()
    {
        return "json-ui";
    }
}
