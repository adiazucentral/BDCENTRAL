import { Response, Request } from 'express';
import { saveOrders } from '../../helpers/central/orders/order';


export const save = async ( req: Request, res: Response ) => {
    const body = req.body;
    const listDocsInserted = await saveOrders(body);
    res.json(listDocsInserted);
}