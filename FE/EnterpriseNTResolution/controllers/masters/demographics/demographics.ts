import { Request, Response } from "express";
import { listDemographicsCube } from "../../../db/masters/demographics/demographics";
import { validateToken } from "../../../tools/common";

export const cube = async( req: Request, res: Response ) => {
    const body = req.body;
    const authLogin = body.authToken;
    if(!authLogin) {
        res.status(401);
    } else {
        const validate = await validateToken(authLogin);
        if(!validate) {
            res.status(401);
        }
        const demos: any = await listDemographicsCube();
        res.json(demos);
    }
}