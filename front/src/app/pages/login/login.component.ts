import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { Subject, takeUntil } from 'rxjs';
import { AppstateService } from 'src/app/shared/services/appstate.service';
import { AuthService } from 'src/app/shared/services/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit, OnDestroy {
  loginForm!: FormGroup;
  readonly _snackBar = inject(MatSnackBar);
  readonly router = inject(Router);
  readonly userAuthService = inject(AuthService);
  readonly appstateService = inject(AppstateService);
  private destroy$ = new Subject<void>();
  constructor() { }

  ngOnInit(): void {
    this.loginForm = new FormGroup({
      login: new FormControl('', [Validators.required]),
      password: new FormControl('', [Validators.required])
    });
  }

  login() {
    this.userAuthService.login(this.loginForm.value).pipe(takeUntil(this.destroy$)).subscribe({
      next: (response) => {
        this._snackBar.open(`Connexion réussie ! Bienvenue sur MDD.`, 'Fermer', {
          duration: 2000,
        });
        this.loadCurrentUser()
        this.router.navigate(['articles']);
      }
      , error: (error) => {
        this._snackBar.open(`Nom d'utilisateur ou mot de passe incorrect !`, 'Fermer', {
          duration: 2000,
        });
      }
    });
  }

  loadCurrentUser() {
    this.userAuthService.getCurrentUser().pipe(takeUntil(this.destroy$)).subscribe({
      next: (res) => {
        this.appstateService.setUserInfo(res);
        this.appstateService.setLoggedIn(true);
      },
      error: (err) => {
        console.error(err);
      }
    });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
