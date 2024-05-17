import { Injectable } from '@angular/core';

import * as ExcelJS from "exceljs/dist/exceljs.min.js";
import * as FileSaver from 'file-saver';

@Injectable({
  providedIn: 'root'
})
export class ExportExcelService {

  constructor() { }

  exportExcel(excelData) {
    var promise = new Promise((resolve, reject) => {
      const title = excelData.title;
      const header = excelData.headers
      const data = excelData.data;
      if (data.length == 0) {
        return '';
      }
      let propertyNames = Object.keys(data[0]);
      let rowWithPropertyNames = header.join(';') + '\n';
      let csvContent = rowWithPropertyNames;
      let rows: string[] = [];
      data.map((item) => {
        let values: string[] = [];
        propertyNames.map((key) => {
          let val: any = item[key];
          if (val !== undefined && val !== null) {
            val = new String(val);
          } else {
            val = '';
          }
          values.push(val);
        });
        rows.push(values.join(';'));
      });
      csvContent += rows.join('\n');
      csvContent = csvContent.replace(/["]+/g, '');
      FileSaver.saveAs(new Blob(["\ufeff"+csvContent], { type: 'application/octet-stream; charset=utf-8' }), title + '.csv');
      resolve(true);
    });
    return promise;
  }
}
