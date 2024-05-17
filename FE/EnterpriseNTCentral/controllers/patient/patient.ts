import { savePatients } from "../../helpers/patient/patient";
import { Response, Request } from 'express';

export const save = async ( req: Request, res: Response ) => {
    const body = req.body;
    const listDocsInserted = await savePatients(body);
    res.json(listDocsInserted);
}