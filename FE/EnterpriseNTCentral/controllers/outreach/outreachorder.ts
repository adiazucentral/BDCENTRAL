import { Request, Response } from "express";
import { listorderfilter } from "../../helpers/outreach/outreachorder";


export const execute = async( req: Request, res: Response ) => {
    
    const body = req.body;
  
    const dataCube: any = await listorderfilter(body);
    res.json(dataCube);
}