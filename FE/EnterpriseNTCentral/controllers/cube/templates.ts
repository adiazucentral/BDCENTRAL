import { Response, Request } from 'express';
import { deleteTemplate, getTemplateById, getTemplates, insertTemplate, updateTemplate } from "../../helpers/cube/templates";

export const get = async ( req: Request, res: Response ) => {
    const templates = await getTemplates();
    res.json(templates);
}

export const insert = async( req: Request, res: Response ) => {
    const resp: any = await insertTemplate(req.body);
    res.json(resp);
}

export const getById = async( req: Request, res: Response ) => {
    const { id } = req.params;
    const template: any = await getTemplateById(id);
    res.json(template);
}

export const update = async( req: Request, res: Response ) => {
    const resp: any = await updateTemplate(req.body);
    res.json(resp);
}

export const deleteT = async( req: Request, res: Response ) => {
    const { id } = req.params;
    const resp: any = await deleteTemplate(id);
    res.json(resp);
}