import { Response, Request } from 'express';
import { getDataCube } from '../../helpers/cube/cube';

export const execute = async( req: Request, res: Response ) => {
    const body = req.body;
    const token = req.get('Authorization');
    const dataCube: any = await getDataCube(body, token);
    res.json(dataCube);
}