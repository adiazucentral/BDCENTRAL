import { Injectable } from '@angular/core';
import { environment } from '../../../../environments/environment';
import { HeaderService } from '../header/header.service';

@Injectable({
  providedIn: 'root'
})
export class DemographicsService {

  urlEnterprise: string = environment.Enterprise_URL;
  urlCentral: string = environment.Central_URL;

  constructor(private headers: HeaderService) { }

  getByState( state:number ) {
    return this.headers.get(`${this.urlEnterprise}/demographics/state/${state}`);
  }

  getListCube(json:any) {
    return this.headers.post('', `${this.urlCentral}/demographics/cube`, {...json});
  }

  getDemographicsALL(token:string) {
    return this.headers.getToken(`${this.urlEnterprise}/demographics/all`, token);
  }

  getDemographicstrue(token:string) {
    return this.headers.getToken(`${this.urlEnterprise}/demographics/filter/state/true`, token);
  }

}
