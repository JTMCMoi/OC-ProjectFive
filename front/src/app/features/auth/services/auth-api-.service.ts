import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { LoginRequestDto } from '../../../shared/models/login-request.model';
import { RegisterRequestDto } from '../../../shared/models/register-request.model';
import { TokenResponseDto } from '../../../shared/models/token-response.model';

@Injectable({
  providedIn: 'root'
})
export class AuthApiService {
  private readonly API = 'http://localhost:8080/api/auth';

  constructor(private http: HttpClient) {}

  login(payload: LoginRequestDto): Observable<TokenResponseDto> {
    return this.http.post<TokenResponseDto>(`${this.API}/login`, payload);
  }

  register(payload: RegisterRequestDto): Observable<TokenResponseDto> {
    return this.http.post<TokenResponseDto>(`${this.API}/register`, payload);
  }
}
