import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ArticleResponse } from 'src/app/shared/models/articleResponse';
import { CommentResponse } from 'src/app/shared/models/commentResponse';
import { ArticleService } from '../services/article.service';
import { Subject, takeUntil } from 'rxjs';

@Component({
  selector: 'app-article-detail',
  templateUrl: './article-detail.component.html',
  styleUrls: ['./article-detail.component.scss']
})
export class ArticleDetailComponent implements OnInit, OnDestroy {
  article?: ArticleResponse;
  comments: CommentResponse[] = [];
  commentContent = '';
  readonly activatedRoute = inject(ActivatedRoute);
  articleId!: number;
  readonly articleService = inject(ArticleService);
  private destroy$ = new Subject<void>();
  constructor() { }

  ngOnInit(): void {
    this.activatedRoute.params.pipe(takeUntil(this.destroy$)).subscribe(params => {
      this.articleId = +params['id']; // cast to number
      this.loadArticle(this.articleId);
      this.loadComments(this.articleId);
    });
  }
  sendComment() {
    if (!this.article || !this.commentContent.trim()) return;

    this.articleService.addComment(this.articleId, this.commentContent).pipe(takeUntil(this.destroy$)).subscribe(() => {
      this.commentContent = '';
      this.loadComments(this.articleId);
    });
  }

  loadComments(articleId: number) {
    this.articleService.getComments(articleId).pipe(takeUntil(this.destroy$)).subscribe(data => this.comments = data);
  }

  loadArticle(id: number) {
    this.articleService.getArticleById(id).pipe(takeUntil(this.destroy$)).subscribe((response: ArticleResponse) => {
      this.article = response;
    });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
