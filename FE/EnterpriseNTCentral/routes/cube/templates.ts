import { Router } from "express";
import { deleteT, get, getById, insert, update } from "../../controllers/cube/templates";
import validate from "../../middleware/validate";
import { body } from "express-validator";
const router = Router();

router.get('/', get);

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

router.get('/:id', getById);
router.delete('/:id', deleteT);

export default router;