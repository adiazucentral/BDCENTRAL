import { Router } from "express";
import { get } from "../../controllers/cube/templates";
import validate from "../../middleware/validate";
import { body } from 'express-validator';
import { execute } from "../../controllers/cube/cube";
const router = Router();

router.patch('/execute', [ validate([
    body('init').notEmpty(),
    body('end').notEmpty()
])] , execute);

export default router;