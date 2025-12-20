import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PostCreate } from 'src/app/shared/models/posts/post-create-request.model';
import { PostResponse } from 'src/app/shared/models/posts/post-response.model';

@Injectable({ providedIn: 'root' })
export class PostApiService {

  private readonly baseUrl = '/api/posts';

  constructor(private http: HttpClient) {}

  getAll(): Observable<PostResponse[]> {
    return this.http.get<PostResponse[]>(this.baseUrl);
  }

  getById(id: number): Observable<PostResponse> {
    return this.http.get<PostResponse>(`${this.baseUrl}/${id}`);
  }

  create(payload: PostCreate): Observable<PostResponse> {
    return this.http.post<PostResponse>(this.baseUrl, payload);
  }
}
