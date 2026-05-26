import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Topic } from '../interfaces/topic.interface';

@Injectable({
  providedIn: 'root'
})
export class TopicService {

  private pathService = 'api/topics';

  constructor(private httpClient: HttpClient) {}

  public getAllTopics(): Observable<Topic[]> {
    return this.httpClient.get<Topic[]>(this.pathService);
  }

  public subscribe(topicId: number): Observable<void> {
    return this.httpClient.post<void>(`${this.pathService}/${topicId}/subscribe`, {});
  }

  public unsubscribe(topicId: number): Observable<void> {
    return this.httpClient.delete<void>(`${this.pathService}/${topicId}/unsubscribe`);
  }

  public getMySubscriptions(): Observable<Topic[]> {
    return this.httpClient.get<Topic[]>(`${this.pathService}/me`);
  }
}