import { Router } from "express";
import { body } from 'express-validator';
import validate from "../../middleware/validate";
import { sendAppointment, sendResults } from "../../controllers/tools/email";
const router = Router();

router.post('/results', [ validate([
    body('recipients').notEmpty(),
    body('subject').notEmpty(),
    body('body').notEmpty(),
    body('order').notEmpty(),
    body('user').notEmpty(),
])] , sendResults);

router.post('/appointment', [ validate([
    body('recipients').notEmpty(),
    body('subject').notEmpty(),
    body('body').notEmpty(),
    body('order').notEmpty(),
    body('user').notEmpty(),
])] , sendAppointment);


export default router;