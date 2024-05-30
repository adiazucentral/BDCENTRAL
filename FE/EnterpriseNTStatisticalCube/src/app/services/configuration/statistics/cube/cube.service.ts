import { Injectable } from '@angular/core';
import { environment } from '../../../../../environments/environment';
import { HeaderService } from '../../header/header.service';

@Injectable({
  providedIn: 'root'
})
export class CubeService {
 
  url: string = environment.Central_URL;

  constructor(private headers: HeaderService) { }

  execute( data:any, token: string ) {
    return this.headers.patch(token, `${this.url}/cube/execute`, {...data});
  }

  insert( data:any ) {
    return this.headers.post('', `${this.url}/template`, {...data});
  }

  update( data:any ) {
    return this.headers.put('', `${this.url}/template`, {...data});
  }

  get() {
    return this.headers.get(`${this.url}/template`);
  }

  getById( id:any ) {
    return this.headers.get(`${this.url}/template/${id}`);
  }

  delete(id:any) {
    return this.headers.delete(`${this.url}/template/${id}`);
  }
}
