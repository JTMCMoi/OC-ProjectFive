import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ThemeResponse } from 'src/app/shared/models/themeResponse';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ThemeService {
  private themeUrl = `${environment.apiUrl}/theme`;

  constructor(private http: HttpClient) { }
  
  getThemes(): Observable<ThemeResponse[]> {
    return this.http.get<ThemeResponse[]>(this.themeUrl, { withCredentials: true });
  }

  subscribe(themeId: number): Observable<void> {
    return this.http.put<void>(`${this.themeUrl}/${themeId}/subscribe`, {}, { withCredentials: true });
  }

  unsubscribe(themeId: number): Observable<void> {
    return this.http.put<void>(`${this.themeUrl}/${themeId}/unsubscribe`, { withCredentials: true });
  }
}
