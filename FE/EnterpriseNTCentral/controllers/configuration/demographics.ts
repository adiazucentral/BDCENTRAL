import { Request, Response } from "express";
import { loadListDemographics } from "../../helpers/configuration/demographics";

export const listForCube = async( req: Request, res: Response ) => {
    const body = req.body;
    const demographics = await loadListDemographics(body.demos);
    res.json(demographics);
}