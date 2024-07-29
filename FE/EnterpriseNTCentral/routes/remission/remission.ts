import { Router } from "express";
import { findRemission, insert } from "../../controllers/remission/remission";
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


router.patch('/', [ validate([
    body('order').notEmpty(),
    body('sample').notEmpty(),
    body('branch').notEmpty(),
])] , findRemission);

export default router;