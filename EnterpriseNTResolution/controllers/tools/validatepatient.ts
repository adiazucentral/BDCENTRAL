
import { Request, Response } from 'express';
import { validatePatient } from '../../models/tools/validatepatients';

export const integration = async( req: Request<{}, {}, ValidatePatient>, res: Response ) => {
    const body:ValidatePatient = req.body;
    const resp:any = await validatePatient(body);
    res.json(resp);
}