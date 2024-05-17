import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { HeadersService } from '../../headers.service';

@Injectable({
  providedIn: 'root'
})
export class DemographicsService {

  urlQueries: string = environment.urlQueries;

  constructor(private headers: HeadersService) { }

  getByState( state:number ) {
    console.log('environment.urlQueries', this.urlQueries);
    return this.headers.get(`${this.urlQueries}/api/demographics/state/${state}`);
  }

  getListCube(json:any) {
    return this.headers.post('', `${this.urlQueries}/api/demographics/cube`, {...json});
  }
}
