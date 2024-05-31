import { Injectable } from '@angular/core';
import { environment } from '../../../../environments/environment';
import { HeaderService } from '../header/header.service';

@Injectable({
  providedIn: 'root'
})
export class AreaService {

  enterpriseURL: string = environment.Enterprise_URL;

  constructor(private headers: HeaderService) { }

  get( token:string ) {
    return this.headers.getToken(`${this.enterpriseURL}/areas`, token);
  }
}
