import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { HeadersService } from '../../headers.service';

@Injectable({
  providedIn: 'root'
})
export class CubeService {

  urlQueries: string = environment.urlQueries;

  constructor(private headers: HeadersService) { }

  execute( data:any ) {
    return this.headers.patch('', `${this.urlQueries}/api/cube/execute`, {...data});
  }

  insert( data:any ) {
    return this.headers.post('', `${this.urlQueries}/api/cube`, {...data});
  }

  update( data:any ) {
    return this.headers.put('', `${this.urlQueries}/api/cube`, {...data});
  }

  get( data:any ) {
    return this.headers.post('', `${this.urlQueries}/api/cube/templates`, {...data});
  }

  getById( data:any ) {
    return this.headers.post('', `${this.urlQueries}/api/cube/template`, {...data});
  }

  delete(id:any) {
    return this.headers.delete(`${this.urlQueries}/api/cube/${id}`);
  }
}
