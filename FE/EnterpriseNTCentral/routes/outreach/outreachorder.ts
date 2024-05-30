import { Router } from "express";
import { execute } from "../../controllers/outreach/outreachorder";

const router = Router();

router.patch('/filter', execute);



export default router;