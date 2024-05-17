import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { HeadersService } from './headers.service';

@Injectable({
  providedIn: 'root'
})
export class BillingService {

  url: string = environment.url;

  constructor(private headers: HeadersService) { }

  getLabserving( init, end, type, profiles ) {
    return this.headers.get(`${this.url}/api/reports/billing/${init}/${end}/${type}/${profiles}`);
  }

  get( init, end, type ) {
    return this.headers.get(`${this.url}/api/reports/ordersFacturation/${init}/${end}/${type}`);
  }
}
