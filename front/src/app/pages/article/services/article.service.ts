import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ArticleResponse } from 'src/app/shared/models/articleResponse';
import { CommentResponse } from 'src/app/shared/models/commentResponse';
import { CreateArticleRequest } from 'src/app/shared/models/createArticleRequest';
import { CreateArticleResponse } from 'src/app/shared/models/createArticleResponse';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ArticleService {
  private articleUrl = `${environment.apiUrl}/article`;

  constructor(private http: HttpClient) { }

  createArticle(article: CreateArticleRequest): Observable<CreateArticleResponse> {
    return this.http.post<CreateArticleResponse>(`${this.articleUrl}`, article, { withCredentials: true });
  }
   
  getArticles(order: 'asc' | 'desc' = 'desc'): Observable<ArticleResponse[]> {
   return this.http.get<ArticleResponse[]>(`${this.articleUrl}?sort=${order}`, { withCredentials: true });
  }

  getArticleById(id: number): Observable<ArticleResponse> {
    return this.http.get<ArticleResponse>(`${this.articleUrl}/${id}`, { withCredentials: true });
  }
  getComments(articleId: number): Observable<CommentResponse[]> {
    return this.http.get<CommentResponse[]>(`${this.articleUrl}/${articleId}/comments`, { withCredentials: true });
  }

  addComment(articleId: number, content: string): Observable<void> {
    return this.http.post<void>(`${this.articleUrl}/${articleId}/comments`, { content }, { withCredentials: true });
  }
}
