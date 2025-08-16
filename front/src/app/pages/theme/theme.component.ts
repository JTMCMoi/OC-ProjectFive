import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { ThemeResponse } from 'src/app/shared/models/themeResponse';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Subject, takeUntil } from 'rxjs';
import { ThemeService } from '../../shared/services/theme.service';

@Component({
  selector: 'app-theme',
  templateUrl: './theme.component.html',
  styleUrls: ['./theme.component.scss']
})
export class ThemeComponent implements OnInit, OnDestroy {


  themes: ThemeResponse[] = [];
  readonly _snackbar = inject(MatSnackBar);
  readonly themeService = inject(ThemeService);
  private destroy$ = new Subject<void>();

  constructor() { }

  ngOnInit(): void {
    this.loadThemeList();
  }

  toggleSubscription(theme: ThemeResponse): void {
    this.themeService.subscribe(theme.id).pipe(takeUntil(this.destroy$)).subscribe(
      {
        next: () => {
          theme.isSubscribed = false
          this._snackbar.open('Vous vous êtes abonné(e) avec succès.', 'Fermer', {
            duration: 2000,
          });
          this.loadThemeList();
        },
        error: (err) => {
          this._snackbar.open('Une erreur est survenue lors de l’abonnement.', 'Fermer', {
            duration: 2000,
          });
        }
      });
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
