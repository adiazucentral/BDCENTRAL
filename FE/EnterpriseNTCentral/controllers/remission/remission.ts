import { Response, Request } from 'express';
import { insertRemission } from "../../helpers/remission/remission";

export const insert = async( req: Request, res: Response ) => {
    const resp: any = await insertRemission(req.body);
    res.json(resp);
}