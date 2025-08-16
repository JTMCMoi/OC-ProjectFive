import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { SubscriptionResponse } from '../models/subscriptionResponse';

@Injectable({
  providedIn: 'root'
})
export class SubscriptionService {
  private subscriptionUrl = `${environment.apiUrl}/subscription`;

  constructor(private http: HttpClient) { }


  getUserSubscriptions(): Observable<SubscriptionResponse[]> {
    return this.http.get<SubscriptionResponse[]>(this.subscriptionUrl, {
      withCredentials: true
    });
  }

  unsubscribe(themeId: number): Observable<void> {
    return this.http.delete<void>(`${this.subscriptionUrl}/${themeId}`, {
      withCredentials: true
    });
  }

}
