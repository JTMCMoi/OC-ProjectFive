import { Location } from '@angular/common';
import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ArticleService } from '../services/article.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { CreateArticleResponse } from 'src/app/shared/models/createArticleResponse';
import { Subject, takeUntil } from 'rxjs';
import { ThemeResponse } from 'src/app/shared/models/themeResponse';
import { ThemeService } from 'src/app/shared/services/theme.service';

@Component({
  selector: 'app-create-article',
  templateUrl: './create-article.component.html',
  styleUrls: ['./create-article.component.scss']
})
export class CreateArticleComponent implements OnInit, OnDestroy {

  form!: FormGroup;
  readonly articleService = inject(ArticleService);
  readonly _snackBar = inject(MatSnackBar);
  readonly themeService = inject(ThemeService);
  // themes = [
  //   { id: 1, label: 'Développement' },
  //   { id: 2, label: 'Design' },
  //   { id: 3, label: 'Marketing' },
  // ];
  themes: ThemeResponse[] = [];
  private destroy$ = new Subject<void>();

  constructor(
    private router: Router,
    private location: Location
  ) { }

  ngOnInit(): void {
    this.loadThemeList();
    this.form = new FormGroup({
      theme: new FormControl('', Validators.required),
      title: new FormControl('', Validators.required),
      content: new FormControl('', Validators.required),
    })
  }

  create(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.articleService.createArticle(this.form.value)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (res: CreateArticleResponse) => {
          this._snackBar.open(`${res.message}`, 'Fermer', {
            duration: 2000,
          });
          this.router.navigate(['/articles']);
        },
        error: (err) => {
          this._snackBar.open(`Erreur lors de la création de l'article`, 'Fermer', {
            duration: 2000,
          });
        }
      });
  }

  back(): void {
    this.location.back();
  }

  loadThemeList() {
    this.themeService.getThemes().pipe(takeUntil(this.destroy$)).subscribe(data => {
      this.themes = data;
    });
  }
  
  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
