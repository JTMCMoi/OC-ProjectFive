
import { Injectable } from '@angular/core';
import { BehaviorSubject, of } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import { AuthService } from './auth.service';
import { UserService } from './user.service';

@Injectable({ providedIn: 'root' })
export class AuthBootstrapService {
  private _ready = new BehaviorSubject<boolean>(false);
  ready$ = this._ready.asObservable();

  constructor(
    private authService: AuthService,
    private userService: UserService
  ) {}

  init(): void {
    if (!this.authService.isAuthenticated()) {
      this._ready.next(true); 
      return;
    }

    this.userService.getCurrentUser().pipe(
      tap(user => this.authService.setCurrentUser(user)),
      catchError(() => {
        this.authService.logout();
        return of(null);
      })
    ).subscribe(() => {
      this._ready.next(true); 
    });
  }
}


