import { Request, Response } from "express";
import { listorderfilter, listResults, listResultHistory, listreport } from "../../helpers/outreach/outreachorder";


export const execute = async( req: Request, res: Response ) => {
    const body = req.body;
    const dataOrder: any = await listorderfilter(body);
    res.json(dataOrder);
}

export const getListResult = async( req: Request, res: Response ) => {
    const { order, area } = req.params;
    const template: any = await listResults(order, area);
    res.json(template);
}

export const getlistResultHistory = async( req: Request, res: Response ) => {
    const body = req.body;
    const template: any = await listResultHistory(body);
    res.json(template);
}

export const getListReport = async( req: Request, res: Response ) => {
    const { order } = req.params;
    const template: any = await listreport(order);
    res.json(template);
}


