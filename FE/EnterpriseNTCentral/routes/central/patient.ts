import { Router } from "express";
import { save } from "../../controllers/central/patient";

const router = Router();
router.patch('/', save);

export default router;