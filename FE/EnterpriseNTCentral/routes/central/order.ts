import { Router } from "express";
import { save } from "../../controllers/central/order";

const router = Router();

router.patch('/', save);

export default router;