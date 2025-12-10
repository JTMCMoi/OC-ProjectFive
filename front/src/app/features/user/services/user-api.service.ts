import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from 'src/app/shared/models/user/user.model';

@Injectable({ providedIn: 'root' })
export class UserApiService {

  private readonly baseUrl = '/api/users';

  constructor(private http: HttpClient) {}

  getCurrentUser(): Observable<User> {
    return this.http.get<User>(`${this.baseUrl}/me`);
  }

  /** Profil public */
  getById(id: number): Observable<User> {
    return this.http.get<User>(`${this.baseUrl}/${id}`);
  }

  updateMe(payload: any): Observable<User> {
    return this.http.put<User>(`${this.baseUrl}/me`, payload);
  }

  /** Suppression du compte */
  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}