import * as _ from "lodash";
import mongoose from 'mongoose';
import OrderSchema from "../../../models/central/order";

export const saveOrders = async (listOrdersByYear: OrdersByYear[]) => {
  const listDocsInserted = [];
  for (const list of listOrdersByYear) {
    const docsOrders = await saveOrderByYear(list.orders, list.year);
    listDocsInserted.push({
      year    : list.year,
      orders  : docsOrders
    });
  }
  return listDocsInserted;
}

export const saveOrderByYear = async (orders: OrderBD[], year: Number) => {
  try {
    const options = { upsert: true, new: true };
    let ordersInserted: any[] = [];
    for (const order of orders) {
      order.lab22c6 = new Date(order.lab22c6);
      let filter = { lab22c1: order.lab22c1, lab22c6: { $lte: order.lab22c6 } };
      const nameCollectionOrders = `lab22_${year}`;
      const CollectionOrders = mongoose.model(nameCollectionOrders, OrderSchema);
      try {
        await CollectionOrders.findOneAndUpdate(filter, { $set: { ...order } }, options)
          .then(doc => {
            if (doc) {
              ordersInserted.push({
                lab22c1: doc.lab22c1,
                created: doc.createdAt,
                updated: doc.updatedAt
              });
            }
          }).catch(err => {
            console.error('Error al insertar la orden:', err);
          });
      } catch (error) {
        console.log("Error al guardar ordenes: ", error);
      }
    }
    return ordersInserted;
  } catch (error) {
    console.log("Error al insertar ordenes: ", error);
  }
}

