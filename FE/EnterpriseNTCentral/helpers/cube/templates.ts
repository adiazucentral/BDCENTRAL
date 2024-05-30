import TemplateCollection from "../../models/cube/templates";
import { ObjectId } from "mongodb";

export const getTemplates = async () => {
    try {
        const templates = await TemplateCollection.find({}, { 
            id                      : '$_id',
            name                    : '$lab35c2', 
            idsDemosH               : '$lab35c3',
            idsDemosO               : '$lab35c4', 
            idsDemosR               : '$lab35c5', 
            init                    : '$lab35c6', 
            end                     : '$lab35c7', 
            profiles                : '$lab35c8', 
            ordering                : '$lab35c9', 
            typeAreaTest            : '$lab35c10', 
            jsonFilterAreaTest      : '$lab35c11', 
            jsonFilterDemographics  : '$lab35c12'
        });
        return templates;
    } catch (error) {
        console.log("Error al obtener plantillas: ", error);
    }
}

export const insertTemplate = async (body: any) => {
    const template = await TemplateCollection.create({
        lab35c2     : body.name, 
        lab35c3     : body.idsDemosH, 
        lab35c4     : body.idsDemosO, 
        lab35c5     : body.idsDemosR, 
        lab35c6     : body.init, 
        lab35c7     : body.end, 
        lab35c8     : body.profiles, 
        lab35c9     : body.ordering, 
        lab35c10    : body.typeAreaTest, 
        lab35c11    : JSON.stringify(body.jsonFilterAreaTest), 
        lab35c12    : JSON.stringify(body.jsonFilterDemographics) 
    });
    return template;
}

export const updateTemplate = async (body: any) => {
    const { id, 
        name, 
        idsDemosH, 
        idsDemosO, 
        idsDemosR, 
        init, 
        end, 
        profiles, 
        ordering, 
        typeAreaTest, 
        jsonFilterAreaTest, 
        jsonFilterDemographics 
    } = body;

    const template = await TemplateCollection.updateOne(
        { _id: id }, 
        { 
            lab35c2 : name,  
            lab35c3 : idsDemosH,  
            lab35c4 : idsDemosO,  
            lab35c5 : idsDemosR,  
            lab35c6 : init,  
            lab35c7 : end,  
            lab35c8 : profiles,  
            lab35c9 : ordering,  
            lab35c10: typeAreaTest,  
            lab35c11: JSON.stringify(jsonFilterAreaTest),  
            lab35c12: JSON.stringify(jsonFilterDemographics)  
        });
    return template;
}

export const getTemplateById = async (id: any) => {
    try {
        const template = await TemplateCollection.findOne({ _id: id }).exec();
        return template;
    } catch (error) {
        console.log("Error al obtener plantila: ", error);
    }
}

export const deleteTemplate = async (id: any) => {
    try {
        const idTemplate: ObjectId = ObjectId.createFromHexString(id);
        const template = await TemplateCollection.findByIdAndDelete(idTemplate).exec();
        return template;
    } catch (error) {
        console.log("Error al obtener plantila: ", error);
    }
}