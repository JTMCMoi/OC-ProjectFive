import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { Subject, takeUntil } from 'rxjs';
import { AppstateService } from 'src/app/shared/services/appstate.service';
import { AuthService } from 'src/app/shared/services/auth.service';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit, OnDestroy {
  registerForm!: FormGroup;
  readonly authService = inject(AuthService);
  readonly _snackBar = inject(MatSnackBar);
  readonly router = inject(Router);
  readonly userAuthService = inject(AuthService);
  readonly appstateService = inject(AppstateService);

  private destroy$ = new Subject<void>();
  constructor() { }

  ngOnInit(): void {
    this.registerForm = new FormGroup({
      username: new FormControl('', [Validators.required]),
      password: new FormControl('', [Validators.required, Validators.minLength(8), Validators.pattern(environment.PASSWORD_PATTERN)]),
      email: new FormControl('', [Validators.required, Validators.email])
    });
  }
  register() {
    this.authService.register(this.registerForm.value).pipe(takeUntil(this.destroy$)).subscribe({
      next: (response) => {
        this._snackBar.open(`Inscription réussie ! Bienvenue sur MDD.`, 'Fermer', {
          duration: 2000,
        });
        this.loadCurrentUser()
        this.router.navigate(['articles']);
      }
      , error: (error) => {
        this._snackBar.open(`Erreur lors de l'inscription !`, 'Fermer', {
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
