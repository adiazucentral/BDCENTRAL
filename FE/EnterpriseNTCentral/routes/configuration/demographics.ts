import { Router } from "express";
import { listForCube } from "../../controllers/configuration/demographics";

const router = Router();

router.post('/cube', listForCube);

export default router;