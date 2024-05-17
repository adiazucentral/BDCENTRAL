import { Response, Request } from 'express';
import { saveOrders } from '../../helpers/orders/order';


export const save = async ( req: Request, res: Response ) => {
    const body = req.body;
    const listDocsInserted = await saveOrders(body);
    res.json(listDocsInserted);
}