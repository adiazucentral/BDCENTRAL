import { Router } from "express";
import { body } from 'express-validator';
import validate from "../../middleware/validate";
import { getStatistics } from "../../db/statistics/statistics";

const router = Router();

router.patch('/general', [ validate([
    body('init').notEmpty(),
    body('end').notEmpty()
])] , getStatistics);

export default router;