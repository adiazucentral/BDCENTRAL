import { Router } from "express";
import { save } from "../../controllers/order/order";

const router = Router();

router.patch('/', save);

export default router;