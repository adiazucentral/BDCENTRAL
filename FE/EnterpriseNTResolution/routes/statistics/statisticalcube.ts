import { Router } from "express";
import { body } from 'express-validator';
import { deleteT, execute, insert, get, update, getById } from "../../controllers/statistics/statisticalcube";
import validate from "../../middleware/validate";
const router = Router();

router.patch('/execute', [ validate([
    body('init').notEmpty(),
    body('end').notEmpty()
])] , execute);

router.post('/', [ validate([
    body('name').notEmpty(),
    body('init').notEmpty(),
    body('end').notEmpty()
])] , insert);

router.put('/', [ validate([
    body('name').notEmpty(),
    body('init').notEmpty(),
    body('end').notEmpty()
])] , update);

router.post('/templates', get);
router.post('/template', getById);
router.delete('/:id', deleteT);

export default router;