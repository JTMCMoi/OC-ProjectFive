import { Injectable } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class TokenService {

  private readonly KEY = 'mdd_token';

  setToken(token: string) {
    localStorage.setItem(this.KEY, token);
  }

  getToken(): string | null {
    return localStorage.getItem(this.KEY);
  }

  clearToken() {
    localStorage.removeItem(this.KEY);
  }

isAuthenticated(): boolean {
  const token = this.getToken();
  if (!token) return false;

  try {
    const payload = JSON.parse(atob(token.split('.')[1]));
    if (payload.exp) {
      const exp = payload.exp * 1000;
      return Date.now() < exp;
    }

    // Pas d'exp → on considère que le token est valide
    return true;

  } catch {
    return false;
  }
}

}

