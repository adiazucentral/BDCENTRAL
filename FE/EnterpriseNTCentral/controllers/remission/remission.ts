import { Response, Request } from 'express';
import { getRemission, getResultRemission, insertRemission } from "../../helpers/remission/remission";

export const insert = async( req: Request, res: Response ) => {
    const resp: any = await insertRemission(req.body);
    res.json(resp);
}

export const findRemission = async( req: Request, res: Response ) => {
    const resp: any = await getRemission(req.body);
    res.json(resp);
}

export const getResultsRemission = async( req: Request, res: Response ) => {
    const resp: any = await getResultRemission(req.body);
    res.json(resp);
}