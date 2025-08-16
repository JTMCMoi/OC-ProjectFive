import { inject, Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, CanLoad, Route, Router, RouterStateSnapshot, UrlSegment, UrlTree } from '@angular/router';
import { map, Observable, of, switchMap, take } from 'rxjs';
import { AppstateService } from '../services/appstate.service';
import { AuthService } from '../services/auth.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate, CanLoad {
  appState = inject(AppstateService);
  authService = inject(AuthService);
  router = inject(Router);
  constructor() { }
  private resolveAuth(): Observable<boolean | UrlTree> {
    return this.authService.checkAuthStatus().pipe(
      map(isAuth => isAuth ? true : this.router.createUrlTree(['/login']))
    );
  }

  canActivate(): Observable<boolean | UrlTree> {
    return this.appState.getLoggedIn().pipe(
      take(1),
      switchMap(loggedIn => {
        if (loggedIn) return of(true);
        return this.resolveAuth();
      })
    );
  }

  canLoad(): Observable<boolean | UrlTree> {
    return this.canActivate(); // même logique pour canLoad
  }
}
