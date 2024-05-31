import { Injectable } from '@angular/core';
import { environment } from '../../../../environments/environment';
import { HeaderService } from '../header/header.service';

@Injectable({
  providedIn: 'root'
})
export class DemographicItemService {

  enterpriseURL: string = environment.Enterprise_URL;

  constructor(private headers: HeaderService) { }
  
  getDemographicsItemsAll(token:string, system, demographic) {
    return this.headers.getToken(`${this.enterpriseURL}/centralsystems/standardization/demographics/system/${system}/demographic/${demographic}`, token);
  }
}
