import { Response, Request } from 'express';
import { saveResults } from '../../helpers/central/result/result';

export const save = async ( req: Request, res: Response ) => {
    const body = req.body;
    const listDocsInserted = await saveResults(body);
    res.json(listDocsInserted);
}