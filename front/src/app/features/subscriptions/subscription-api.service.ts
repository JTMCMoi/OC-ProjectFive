import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { SubscriptionResponse } from 'src/app/shared/models/subscriptions/subscription.model';

@Injectable({ providedIn: 'root' })
export class SubscriptionApiService {

  private readonly baseUrl = '/api/subscriptions';

  constructor(private http: HttpClient) {}

  subscribe(topicId: number): Observable<SubscriptionResponse> {
    return this.http.post<SubscriptionResponse>(`${this.baseUrl}/${topicId}`, null);
  }

  unsubscribe(topicId: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${topicId}`);
  }

  getMySubscriptions(): Observable<SubscriptionResponse[]> {
    return this.http.get<SubscriptionResponse[]>(this.baseUrl);
  }
}
