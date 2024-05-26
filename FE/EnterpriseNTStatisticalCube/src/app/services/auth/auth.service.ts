import { Injectable } from '@angular/core';
import { Auth } from '../../intefaces/auth/auth';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { map } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  Security_URL: string;

  constructor(private http: HttpClient) { 
    this.Security_URL = environment.Security_URL;
  }

  login(user: Auth) {
    const authData = {
      ...user
    };
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
    });

    return this.http.post(`${this.Security_URL}/authentication`, authData, { headers }).pipe(map((resp: any) => {
      this.saveToken(resp);
      return resp;
    })
    );
  }

  private saveToken(user: any) {
    sessionStorage.setItem('authToken', user.token);
    sessionStorage.setItem('user', JSON.stringify(user.user));
  }

  isLoggedIn(): boolean {
    const authToken = sessionStorage.getItem('authToken');
    return !!authToken;
  }

}
