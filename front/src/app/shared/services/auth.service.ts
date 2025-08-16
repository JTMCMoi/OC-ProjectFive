import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { LoginRequest } from '../models/loginRequest';
import { RegisterOrUpdateRequest } from '../models/registerRequest';
import { catchError, map, Observable, of } from 'rxjs';
import { AppstateService } from './appstate.service';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private authUrl = `${environment.apiUrl}/auth`;
  appState = inject(AppstateService);
  constructor(private http: HttpClient) { }
  checkAuthStatus(): Observable<boolean> {
    return this.getCurrentUser().pipe(
      map(user => {
        this.appState.setUserInfo(user);
        this.appState.setLoggedIn(true);
        return true;
      }),
      catchError(() => {
        this.appState.clearUserInfo();
        this.appState.clearLoggedIn();
        return of(false);
      })
    );
  }
  login(LoginRequest: LoginRequest) {
    return this.http.post(`${this.authUrl}/login`, LoginRequest, { withCredentials: true });
  }

  register(registerRequest: RegisterOrUpdateRequest) {
    return this.http.post(`${this.authUrl}/register`, registerRequest, { withCredentials: true });
  }

  logout() {
    return this.http.post(`${this.authUrl}/logout`, {}, { withCredentials: true });
  }

  getCurrentUser() {
    return this.http.get(`${this.authUrl}/me`, { withCredentials: true });
  }
}
