import { HttpHeaders } from '@angular/common/http';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class HeadersService {

  constructor(private http: HttpClient) { }

  get(url: string): Observable<any> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      Accept: 'application/json'
    });
    return this.http.get(url, { headers });
  }

  post(token: string, url: string, data: any ): Observable<any>  {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      Authorization: 'Bearer ' + token,
      'X-Requested-With': 'XMLHttpRequest'
    });
    return this.http.post(url, data, {headers});
  }

  put(token: string, url: string, data: any ): Observable<any>  {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'X-Requested-With': 'XMLHttpRequest'
    });
    return this.http.put(url, data, {headers});
  }

  patch(token: string, url: string, data: any ): Observable<any>  {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      Authorization: 'Bearer ' + token,
      'X-Requested-With': 'XMLHttpRequest'
    });
    return this.http.patch(url, data, {headers});
  }

  delete(url: string): Observable<any> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      Accept: 'application/json'
    });
    return this.http.delete(url, { headers });
  }
}
