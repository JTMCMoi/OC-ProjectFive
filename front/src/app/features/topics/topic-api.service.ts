import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Topic } from 'src/app/shared/models/topics/topic.model';

@Injectable({ providedIn: 'root' })
export class TopicApiService {

  private readonly baseUrl = '/api/topics';

  constructor(private http: HttpClient) {}

  getAll(): Observable<Topic[]> {
    return this.http.get<Topic[]>(this.baseUrl);
  }

  getById(id: number): Observable<Topic> {
    return this.http.get<Topic>(`${this.baseUrl}/${id}`);
  }
}
