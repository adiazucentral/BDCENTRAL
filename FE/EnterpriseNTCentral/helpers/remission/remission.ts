
import RemissionCollection from "../../models/results/remission";
import { ObjectId } from "mongodb";


export const insertRemission = async (body: any) => {
    const remission = await RemissionCollection.create({
        lab22c1     : body.order,
        lab39c1     : body.test,
        lab24c1     : body.sample,
        lab05c1_1   : body.branchOrigin,
        lab05c1_2   : body.branchDestination,
        lab57rc1    : 0

    });
    return remission;
}
