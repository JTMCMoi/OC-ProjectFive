import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { AuthApiService } from 'src/app/features/auth/services/auth-api-.service';
import { TokenService } from './token.service';
import { LoginRequest } from 'src/app/shared/models/auth/login-request.model';
import { TokenResponse } from 'src/app/shared/models/auth/token-response.model';
import { RegisterRequest } from 'src/app/shared/models/auth/register-request.model';
import { User } from 'src/app/shared/models/user/user.model';

@Injectable({ providedIn: 'root' })
export class AuthService {

  private currentUserSubject = new BehaviorSubject<User | null>(null);
  currentUser$ = this.currentUserSubject.asObservable();

  constructor(
    private authApi: AuthApiService,
    private token: TokenService
  ) {
    this.loadUserFromToken();
  }

  login(payload: LoginRequest): Observable<TokenResponse> {
    return this.authApi.login(payload).pipe(
      tap(res => this.handleAuthSuccess(res))
    );
  }

  register(payload: RegisterRequest): Observable<TokenResponse> {
    return this.authApi.register(payload).pipe(
      tap(res => this.handleAuthSuccess(res))
    );
  }

  logout(): void {
    this.token.clearToken();
    this.currentUserSubject.next(null);
  }

  isAuthenticated(): boolean {
    return this.token.isAuthenticated();
  }
  get currentUser(): User | null {
    return this.currentUserSubject.value;
  }

  updateCurrentUser(user: User) {
    this.currentUserSubject.next(user);
  }

  private handleAuthSuccess(res: TokenResponse) {
    if (!res?.token) return;

    this.token.setToken(res.token);
    this.loadUserFromToken();
  }

  private loadUserFromToken() {
    const token = this.token.getToken();
    if (!token) return;

    try {
      const payload = JSON.parse(atob(token.split('.')[1]));

      this.currentUserSubject.next({
        id: payload.id,
        username: payload.username,
        email: payload.email
      } as User);

    } catch {
      this.currentUserSubject.next(null);
    }
  }
}
