import { Router } from "express";
import { save } from "../../controllers/central/result";

const router = Router();

router.patch('/', save);

export default router;