import { Router } from "express";
import { body } from 'express-validator';
import validate from "../../middleware/validate";
import { integration } from "../../controllers/tools/validatepatient";
const router = Router();

router.post('/', [ validate([
    body('username').notEmpty(),
    body('password').notEmpty(),
    body('url').notEmpty(),
    body('personalId').notEmpty()
])] , integration);

export default router;