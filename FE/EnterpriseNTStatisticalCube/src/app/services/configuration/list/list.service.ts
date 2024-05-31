import { Injectable } from '@angular/core';
import { environment } from '../../../../environments/environment';
import { HeaderService } from '../header/header.service';

@Injectable({
  providedIn: 'root'
})
export class ListService {

  enterpriseURL: string = environment.Enterprise_URL;

  constructor(private headers: HeaderService) { }
  
  getList(token:string, id) {
    return this.headers.getToken(`${this.enterpriseURL}/lists/${id}`, token);
  }
}
