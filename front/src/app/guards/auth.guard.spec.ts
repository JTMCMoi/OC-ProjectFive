import { TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import { ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';

import { AuthGuard } from './auth.guard';
import { AuthService } from 'src/app/services/auth.service';

const mockRoute = {} as ActivatedRouteSnapshot;
const mockState = {} as RouterStateSnapshot;

describe('AuthGuard (intégration)', () => {
  let guard: AuthGuard;
  let authService: { isTokenValid: jest.Mock };
  let router: { createUrlTree: jest.Mock };

  beforeEach(() => {
    authService = { isTokenValid: jest.fn() };
    router      = { createUrlTree: jest.fn((path) => ({ path })) };

    TestBed.configureTestingModule({
      providers: [
        AuthGuard,
        { provide: AuthService, useValue: authService },
        { provide: Router,      useValue: router },
      ],
    });

    guard = TestBed.inject(AuthGuard);
  });

  // ─── Test 1 ─────────────────────────────────────────────────────────────────

  it('token valide — canActivate doit retourner true', () => {
    authService.isTokenValid.mockReturnValue(true);

    const result = guard.canActivate(mockRoute, mockState);

    expect(result).toBe(true);
    expect(router.createUrlTree).not.toHaveBeenCalled();
  });

  // ─── Test 2 ─────────────────────────────────────────────────────────────────

  it('token invalide — canActivate doit rediriger vers /login', () => {
    authService.isTokenValid.mockReturnValue(false);

    const result = guard.canActivate(mockRoute, mockState);

    expect(result).toEqual({ path: ['/login'] });
    expect(router.createUrlTree).toHaveBeenCalledWith(['/login']);
  });

  // ─── Test 3 ─────────────────────────────────────────────────────────────────

  it('absence de token — canActivate doit rediriger vers /login (pas de navigation vers /)', () => {
    authService.isTokenValid.mockReturnValue(false);

    const result = guard.canActivate(mockRoute, mockState);

    // Ne doit pas laisser passer vers une route protégée
    expect(result).not.toBe(true);
    expect(router.createUrlTree).toHaveBeenCalledWith(['/login']);
  });
});

