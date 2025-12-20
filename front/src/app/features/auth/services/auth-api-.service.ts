import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { LoginRequest } from 'src/app/shared/models/auth/login-request.model';
import { TokenResponse } from 'src/app/shared/models/auth/token-response.model';
import { RegisterRequest } from 'src/app/shared/models/auth/register-request.model';

@Injectable({ providedIn: 'root' })
export class AuthApiService {

  private readonly baseUrl = '/api/auth';

  constructor(private http: HttpClient) {}

  login(payload: LoginRequest): Observable<TokenResponse> {
    return this.http.post<TokenResponse>(`${this.baseUrl}/login`, payload);
  }

  register(payload: RegisterRequest): Observable<TokenResponse> {
    return this.http.post<TokenResponse>(`${this.baseUrl}/register`, payload);
  }
}
