import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Subject, takeUntil } from 'rxjs';
import { SubscriptionResponse } from 'src/app/shared/models/subscriptionResponse';
import { AppstateService } from 'src/app/shared/services/appstate.service';
import { AuthService } from 'src/app/shared/services/auth.service';
import { SubscriptionService } from 'src/app/shared/services/subscription.service';
import { UserService } from 'src/app/shared/services/user.service';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-profil',
  templateUrl: './profil.component.html',
  styleUrls: ['./profil.component.scss']
})
export class ProfilComponent implements OnInit, OnDestroy {
  profilForm!: FormGroup;

  subscriptions: SubscriptionResponse[] = [];

  readonly appstateService = inject(AppstateService);
  readonly userService = inject(UserService);
  readonly _snackBar = inject(MatSnackBar);
  readonly userAuthService = inject(AuthService);
  readonly subscriptionService = inject(SubscriptionService);
  private destroy$ = new Subject<void>();

  constructor() { }

  ngOnInit(): void {

    this.profilForm = new FormGroup({
      username: new FormControl('', Validators.required),
      email: new FormControl('', [Validators.required, Validators.email]),
      password: new FormControl('', [Validators.required, Validators.minLength(8), Validators.pattern(environment.PASSWORD_PATTERN)]),
    });
    this.loadUserInfo();
    this.loadSubscriptions();
  }

  updateProfil() {
    this.userService.updateUserInfo(this.profilForm.value).pipe(takeUntil(this.destroy$)).subscribe({
      next: (res) => {
        this._snackBar.open(`Profil mis à jour avec succès !`, 'Fermer', {
          duration: 2000,
        });
        this.refreshCurrentUserInfo();
        this.loadUserInfo();
      },
      error: (err) => {
        console.error('Erreur lors de la mise à jour du profil', err);
      }
    });
  }

  loadUserInfo() {
    this.appstateService.getUserInfo().pipe(takeUntil(this.destroy$)).subscribe({
      next: (res) => {
        this.profilForm.patchValue({
          username: res?.username,
          email: res?.email,
        });
      },
      error: (err) => {
        console.error(err);
      }
    });
  }
  refreshCurrentUserInfo() {
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
  unsubscribe(themeId: number) {
    this.subscriptionService.unsubscribe(themeId).pipe(takeUntil(this.destroy$)).subscribe({
      next: (res) => {
        this._snackBar.open(`Vous vous êtes désabonné avec succès !`, 'Fermer', {
          duration: 2000,
        });
        this.loadUserInfo();
        this.loadSubscriptions();
      },
      error: (err) => {
        this._snackBar.open(`Une erreur est survenue lors du désabonnement.`, 'Fermer', {
          duration: 2000,
        });
      }
    });
  }

  loadSubscriptions(): void {
    this.subscriptionService.getUserSubscriptions().pipe(takeUntil(this.destroy$)).subscribe(data => {
      this.subscriptions = data;
    });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
