import { Injectable } from '@angular/core';
import { environment } from '../../../../environments/environment';
import { HeaderService } from '../header/header.service';

@Injectable({
  providedIn: 'root'
})
export class TestService {

  enterpriseURL: string = environment.Enterprise_URL;

  constructor(private headers: HeaderService) { }

  getTestArea( token, type, state, area ) {
    return this.headers.getToken(`${this.enterpriseURL}/tests/filter/type/${type}/state/${state}/area/${area}`, token);
  }

  getgroups(token:string) {
    return this.headers.getToken(`${this.enterpriseURL}/groups/tests`, token);
  }

  getTestConfidential(token:string) {
    return this.headers.getToken(`${this.enterpriseURL}/tests/filter/confidentials`, token);
  }
}
