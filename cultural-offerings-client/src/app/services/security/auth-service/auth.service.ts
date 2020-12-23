import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { SignInUser } from 'src/app/model/sign-in-user/sign-in-user';
import { ApiService } from '../api-service/api.service';
import { catchError, map } from 'rxjs/operators';
import { SignInService } from '../sign-in-service/sign-in.service';
import { Observable, of } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  //private access_token = null;
  private signInUrl : string = environment.baseUrl + '/auth/login';

  constructor(private apiService: ApiService, private signInService: SignInService, private router: Router) {}

  headers = new HttpHeaders({
    'Accept': 'application/json',
    'Content-Type': 'application/json'
  });

  signin(user : SignInUser) {
    const body = {
      'email' : user.email,
      'password' : user.password
    };
    return this.apiService.post(this.signInUrl, JSON.stringify(body), this.headers).pipe(
      map((res) => {
        console.log('Login success');
        //this.access_token = res.jwt;
        localStorage.setItem('jwtToken', res.jwt);
        return true;
      })
      );
  }

  logout() {
    this.signInService.changeUserData(null);
    //this.access_token = null;
    localStorage.removeItem('jwtToken');
    this.router.navigate(['/sign-in']);
  }

  tokenIsPresent() {
    let token = this.getToken();
    return token != undefined && token != null;
  }

  getToken() {
    //return this.access_token;
    return localStorage.getItem('jwtToken');
  }

}
