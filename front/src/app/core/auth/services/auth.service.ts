import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { AuthApiService } from 'src/app/features/auth/services/auth-api-.service';
import { UserApiService } from 'src/app/features/user/services/user-api.service';
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
    private userApi: UserApiService,
    private token: TokenService
  ) {}

  login(payload: LoginRequest): Observable<TokenResponse> {
    return this.authApi.login(payload).pipe(
      tap(res => {
        if (!res?.token) return;

        this.token.setToken(res.token);
        this.loadCurrentUser();
      })
    );
  }

  register(payload: RegisterRequest): Observable<TokenResponse> {
    return this.authApi.register(payload).pipe(
      tap(res => {
        if (!res?.token) return;

        this.token.setToken(res.token);
        this.loadCurrentUser();
      })
    );
  }

  loadCurrentUser(): void {
    if (!this.isAuthenticated()) return;

    this.userApi.getCurrentUser().subscribe({
      next: user => this.setCurrentUser(user),
      error: () => this.logout()
    });
  }

  setCurrentUser(user: User | null): void {
    this.currentUserSubject.next(user);
  }

  logout(): void {
    this.token.clearToken();
    this.setCurrentUser(null);
  }

  isAuthenticated(): boolean {
    return this.token.isAuthenticated();
  }

  get currentUser(): User | null {
    return this.currentUserSubject.value;
  }
}
