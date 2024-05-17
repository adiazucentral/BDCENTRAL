import { Request, Response } from 'express';
import { sendEmailAppointment, sendEmailResults } from '../../models/tools/email';

export const sendResults = async( req: Request<{}, {}, Email>, res: Response ) => {
    const body:Email = req.body;
    const resp:any = await sendEmailResults(body);
    res.json(resp);
}

export const sendAppointment = async( req: Request<{}, {}, Email>, res: Response ) => {
    const body:Email = req.body;
    const resp:any = await sendEmailAppointment(body);
    res.json(resp);
}