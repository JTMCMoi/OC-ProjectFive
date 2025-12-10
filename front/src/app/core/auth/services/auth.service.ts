import { Injectable } from '@angular/core';
import { Observable, tap } from 'rxjs';
import { AuthApiService } from 'src/app/features/auth/services/auth-api-.service';
import { TokenService } from './token.service';
import { LoginRequest } from 'src/app/shared/models/auth/login-request.model';
import { TokenResponse } from 'src/app/shared/models/auth/token-response.model';
import { RegisterRequest } from 'src/app/shared/models/auth/register-request.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(
    private authApi: AuthApiService,
    private token: TokenService
  ) {}

  login(payload: LoginRequest): Observable<TokenResponse> {
    return this.authApi.login(payload).pipe(
      tap(res => {
        if (res?.token) {
          this.token.setToken(res.token);
        }
      })
    );
  }

  register(payload: RegisterRequest): Observable<TokenResponse> {
    return this.authApi.register(payload).pipe(
      tap(res => {
        if (res?.token) {
          this.token.setToken(res.token);
        }
      })
    );
  }

  logout(): void {
    this.token.clearToken();
  }

  isAuthenticated(): boolean {
    return this.token.isAuthenticated();
  }
}
