import { Response, Request } from 'express';
import { savePatients } from '../../helpers/central/patient/patient';

export const save = async ( req: Request, res: Response ) => {
    const body = req.body;
    const listDocsInserted = await savePatients(body);
    res.json(listDocsInserted);
}