import { Router } from "express";
import { insert } from "../../controllers/remission/remission";
import validate from "../../middleware/validate";
import { body } from "express-validator";
const router = Router();

router.post('/', [ validate([
    body('order').notEmpty(),
    body('test').notEmpty(),
    body('sample').notEmpty(),
    body('branchOrigin').notEmpty(),
    body('branchDestination').notEmpty()
])] , insert);

export default router;