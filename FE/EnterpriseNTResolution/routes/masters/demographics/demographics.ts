import { Router } from "express";
import { cube } from "../../../controllers/masters/demographics/demographics";

const router = Router();

router.post('/cube', cube);

export default router;