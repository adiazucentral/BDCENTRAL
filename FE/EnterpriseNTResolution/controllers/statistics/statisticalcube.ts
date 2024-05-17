import { Request, Response } from "express";
import { deleteTemplate, getDataCube, getTemplate, getTemplateById, insertTemplate, updateTemplate } from "../../db/statistics/statisticalcube";
import { validateToken } from "../../tools/common";

export const execute = async( req: Request, res: Response ) => {
    const body = req.body;
    const authLogin = body.authToken;
    if(!authLogin) {
        res.status(401);
    } else {
        const validate = await validateToken(authLogin);
        if(!validate) {
            res.status(401);
        }
        const dataCube: any = await getDataCube(body);
        res.json(dataCube);
    }
}

export const insert = async( req: Request, res: Response ) => {
    const body = req.body;
    const authLogin = body.authToken;
    if(!authLogin) {
        res.status(401);
    } else {
        const validate = await validateToken(authLogin);
        if(!validate) {
            res.status(401);
        }
        const resp: any = await insertTemplate(body);
        res.json(resp);
    }
}

export const get = async( req: Request, res: Response ) => {
    const body = req.body;
    const authLogin = body.authToken;
    if(!authLogin) {
        res.status(401);
    } else {
        const validate = await validateToken(authLogin);
        if(!validate) {
            res.status(401);
        }
        const templates: any = await getTemplate();
        res.json(templates);
    }
}

export const getById = async( req: Request, res: Response ) => {
    const body = req.body;
    const authLogin = body.authToken;
    if(!authLogin) {
        res.status(401);
    } else {
        const validate = await validateToken(authLogin);
        if(!validate) {
            res.status(401);
        }
        const templates: any = await getTemplateById(+body.id);
        res.json(templates);
    }
}

export const update = async( req: Request, res: Response ) => {
    const body = req.body;
    const authLogin = body.authToken;
    if(!authLogin) {
        res.status(401);
    } else {
        const validate = await validateToken(authLogin);
        if(!validate) {
            res.status(401);
        }
        const resp: any = await updateTemplate(body);
        res.json(resp);
    }
}

export const deleteT = async( req: Request, res: Response ) => {
    const { id } = req.params;
    const resp: any = await deleteTemplate(+id);
    res.json(resp);
}
