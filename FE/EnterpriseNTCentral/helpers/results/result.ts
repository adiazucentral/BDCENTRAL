import * as _ from "lodash";
import mongoose from 'mongoose';
import ResultSchema from "../../models/result/result";

export const saveResults = async (listResultsByYear: ResultsByYear[]) => {
  const listDocsInserted = [];
  for (const list of listResultsByYear) {
    const docsResults = await saveResultByYear(list.results, list.year);
    listDocsInserted.push({
      year      : list.year,
      results   : docsResults
    });
  }
  return listDocsInserted;
}

export const saveResultByYear = async (results: ResultDB[], year: Number) => {
  try {
    const options = { upsert: true, new: true };
    let ResultsInserted: any[] = [];
    for (const result of results) {
      result.lab57c18 = new Date(result.lab57c18);
      let filter = { lab22c1: result.lab22c1, 'lab39.lab39c1': result.lab39.lab39c1, lab57c18: { $lte: result.lab57c18 } };
      const nameCollectionResults = `lab57_${year}`;
      const CollectionResults = mongoose.model(nameCollectionResults, ResultSchema);
      try {
        await CollectionResults.findOneAndUpdate(filter, { $set: { ...result } }, options)
          .then(doc => {
            if (doc) {
              ResultsInserted.push({
                lab22c1: doc.lab22c1,
                lab39c1: doc.lab39.lab39c1,
                created: doc.createdAt,
                updated: doc.updatedAt
              });
            }
          }).catch(err => {
            console.error('Error al insertar el resultado:', err);
          });
      } catch (error) {
        console.log("Error al guardar resultados: ", error);
      }
    }
    return ResultsInserted;
  } catch (error) {
    console.log("Error al insertar resultados: ", error);
  }
}

