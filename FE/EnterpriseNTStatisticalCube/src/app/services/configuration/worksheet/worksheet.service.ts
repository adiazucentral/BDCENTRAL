import { Injectable } from '@angular/core';
import { HeaderService } from '../header/header.service';
import { environment } from '../../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class WorksheetService {

  enterpriseURL: string = environment.Enterprise_URL;

  constructor(private headers: HeaderService) { }

  getWorkSheet( token: string ) {
    return this.headers.getToken(`${this.enterpriseURL}/worksheets`, token);
  }
}
