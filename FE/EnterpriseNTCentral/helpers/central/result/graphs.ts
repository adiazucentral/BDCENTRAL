import * as fs from 'fs';
import * as path from 'path';
import { ResultDB } from '../../../interfaces/result/result';

export const processGraphs = async (result: ResultDB) => {
    if (result.doc03.length > 0) {
      for (let graph of result.doc03) {

        if(graph.doc03c2 !== null && graph.doc03c2 !== undefined && graph.doc03c2 !== "") {
            await saveGraph(graph, 1).then( resolve => {
                graph = resolve;
            });
        }

        if(graph.doc03c4 !== null && graph.doc03c4 !== undefined && graph.doc03c4 !== "") {
            await saveGraph(graph, 2).then( resolve => {
                graph = resolve;
            });
        }

        if(graph.doc03c6 !== null && graph.doc03c6 !== undefined && graph.doc03c6 !== "") {
            await saveGraph(graph, 3).then( resolve => {
                graph = resolve;
            });
        }

        if(graph.doc03c8 !== null && graph.doc03c8 !== undefined && graph.doc03c8 !== "") {
            await saveGraph(graph, 4).then( resolve => {
                graph = resolve;
            });
        }

        if(graph.doc03c10 !== null && graph.doc03c10 !== undefined && graph.doc03c10 !== "") {
            await saveGraph(graph, 5).then( resolve => {
                graph = resolve;
            });
        }
      }
      return result;
    } else {
      return result;
    }
  }
  
  const saveGraph = async (graph: Graphs, image: number) => {
    return new Promise<Graphs>((resolve, reject) => {

        let base64Data = "";
        let fileName = "";

        switch (image) {
            case 1:
                base64Data  = graph.doc03c2;
                fileName    = graph.doc03c1.replace(/\s/g, "");
                break;
            case 2:
                base64Data  = graph.doc03c4;
                fileName    = graph.doc03c3.replace(/\s/g, "");
                break;
            case 3:
                base64Data  = graph.doc03c6;
                fileName    = graph.doc03c5.replace(/\s/g, "");
                break;
            case 4:
                base64Data  = graph.doc03c8;
                fileName    = graph.doc03c7.replace(/\s/g, "");
                break;
            case 5:
                base64Data  = graph.doc03c10;
                fileName    = graph.doc03c9.replace(/\s/g, "");
                break;
            default:
                break;
        }
    
      const route = `Graficas/${graph.lab22c1}/${graph.lab39c1}`;
    
      const outputFolder: string = path.join(__dirname, route);
    
      if (!fs.existsSync(outputFolder)) {
        fs.mkdirSync(outputFolder, { recursive: true });
      }
    
      const outputPath: string = path.join(outputFolder, fileName);
    
      const bufferData: Buffer = Buffer.from(base64Data, 'base64');
    
      fs.writeFile(outputPath, bufferData, (err: NodeJS.ErrnoException | null) => {
        if (err) {
          console.error(`Error al escribir la grafica: ${fileName}`, err);
          resolve(graph);
        } else {
            switch (image) {
                case 1:
                    graph.routeBD1 = outputPath;
                    break;
                case 2:
                    graph.routeBD2 = outputPath;
                    break;
                case 3:
                    graph.routeBD3 = outputPath;
                    break;
                case 4:
                    graph.routeBD4 = outputPath;
                    break;
                case 5:
                    graph.routeBD5 = outputPath;
                    break;
                default:
                    break;
            }
            console.log('Grafica guardada con éxito en:', outputPath);
            resolve(graph);
        }
      });
    });
}