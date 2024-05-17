import { Router } from "express";
import { save } from "../../controllers/patient/patient";

const router = Router();
router.patch('/', save);

export default router;