import { Router } from "express";
import { save } from "../../controllers/result/result";

const router = Router();

router.patch('/', save);

export default router;