import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CommentCreate } from 'src/app/shared/models/comments/comment-create-requesst.model';
import { CommentResponse } from 'src/app/shared/models/comments/comment-response.model';

@Injectable({ providedIn: 'root' })
export class CommentApiService {

  private readonly baseUrl = '/api/comments';

  constructor(private http: HttpClient) {}

  getByPost(postId: number): Observable<CommentResponse[]> {
    return this.http.get<CommentResponse[]>(`${this.baseUrl}/post/${postId}`);
  }

  create(payload: CommentCreate): Observable<CommentResponse> {
    return this.http.post<CommentResponse>(this.baseUrl, payload);
  }

    delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
