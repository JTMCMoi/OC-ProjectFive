import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Post, PostDetail, PostComment } from '../interfaces/post.interface';

@Injectable({
  providedIn: 'root'
})
export class PostService {

  private pathService = 'api/posts';

  constructor(private httpClient: HttpClient) {}

  public getFeed(sort: string = 'desc'): Observable<Post[]> {
    return this.httpClient.get<Post[]>(`${this.pathService}/feed?sort=${sort}`);
  }

  public createPost(request: { topicId: number; title: string; content: string }): Observable<Post> {
    return this.httpClient.post<Post>(`${this.pathService}`, request);
  }

  public getPostById(id: number): Observable<PostDetail> {
    return this.httpClient.get<PostDetail>(`${this.pathService}/${id}`);
  }

  public addComment(postId: number, request: { content: string }): Observable<PostComment> {
    return this.httpClient.post<PostComment>(`${this.pathService}/${postId}/comments`, request);
  }
}