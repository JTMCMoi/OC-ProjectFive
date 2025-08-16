import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ArticleService } from '../services/article.service';
import { ArticleResponse } from 'src/app/shared/models/articleResponse';
import { Subject, takeUntil } from 'rxjs';

@Component({
  selector: 'app-article-list',
  templateUrl: './article-list.component.html',
  styleUrls: ['./article-list.component.scss']
})
export class ArticleListComponent implements OnInit, OnDestroy {

  readonly router = inject(Router);
  articles: ArticleResponse[] = [];
  readonly articleService = inject(ArticleService);
  sortOrder: 'asc' | 'desc' = 'desc';
  private destroy$ = new Subject<void>();

  constructor() { }

  ngOnInit(): void {
    this.loadArticles();
  }

  create() {
    this.router.navigate(['articles/create']);
  }

  goToArticle(articleId: number) {
    this.router.navigate([`articles/${articleId}`]);
  }

  loadArticles() {
    this.articleService.getArticles(this.sortOrder).pipe(takeUntil(this.destroy$)).subscribe((response: ArticleResponse[]) => {
      this.articles = response;
    });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
