import { Injectable } from '@angular/core';
import { Observable, tap } from 'rxjs';
import { AuthApiService } from 'src/app/features/auth/services/auth-api-.service';
import { LoginRequestDto } from 'src/app/shared/models/login-request.model';
import { RegisterRequestDto } from 'src/app/shared/models/register-request.model';
import { TokenResponseDto } from 'src/app/shared/models/token-response.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly TOKEN_KEY = 'mdd_token';

  constructor(private authApi: AuthApiService) {}

  login(payload: LoginRequestDto): Observable<TokenResponseDto> {
    return this.authApi.login(payload).pipe(
      tap(res => {
        if (res && res.token) {
          localStorage.setItem(this.TOKEN_KEY, res.token);
        }
      })
    );
  }

  register(payload: RegisterRequestDto): Observable<TokenResponseDto> {
    return this.authApi.register(payload).pipe(
      tap(res => {
        if (res && res.token) {
          localStorage.setItem(this.TOKEN_KEY, res.token);
        }
      })
    );
  }

  logout(): void {
    localStorage.removeItem(this.TOKEN_KEY);
  }

  getToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  isAuthenticated(): boolean {
    return !!this.getToken();
  }
}
