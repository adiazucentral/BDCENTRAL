import { Router } from "express";
import { execute , getListResult, getlistResultHistory, getListReport } from "../../controllers/outreach/outreachorder";

const router = Router();

router.patch('/filter', execute);

router.get('/results/order/:order/:area', getListResult);

router.patch('/orders/results/history', getlistResultHistory);

router.get('/orders/report/:order', getListReport);

export default router;