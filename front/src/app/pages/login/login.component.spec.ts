import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { NO_ERRORS_SCHEMA } from '@angular/core';

import { LoginComponent } from './login.component';
import { AuthService } from 'src/app/services/auth.service';

const mockAuthResponse = { token: 'jwt-abc', type: 'Bearer', id: 1, username: 'david', email: 'david@test.com' };

describe('LoginComponent (intégration)', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;

  let authService: { login: jest.Mock };
  let router: { navigate: jest.Mock };

  beforeEach(async () => {
    authService = { login: jest.fn() };
    router      = { navigate: jest.fn() };

    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      imports: [ReactiveFormsModule],
      providers: [
        { provide: AuthService, useValue: authService },
        { provide: Router,      useValue: router },
      ],
      schemas: [NO_ERRORS_SCHEMA],
    }).compileComponents();

    fixture   = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  // ─── Test 1 ─────────────────────────────────────────────────────────────────

  it('login réussi — doit appeler authService.login() et naviguer vers /', fakeAsync(() => {
    authService.login.mockReturnValue(of(mockAuthResponse));

    component.loginForm.patchValue({ emailOrUsername: 'david', password: 'Password1!' });
    component.onSubmit();
    tick();

    expect(authService.login).toHaveBeenCalledWith({ emailOrUsername: 'david', password: 'Password1!' });
    expect(router.navigate).toHaveBeenCalledWith(['/']);
  }));

  // ─── Test 2 ─────────────────────────────────────────────────────────────────

  it('login échoué — doit afficher un message d\'erreur et ne pas naviguer', fakeAsync(() => {
    authService.login.mockReturnValue(throwError(() => ({ error: { message: 'Identifiants invalides' } })));

    component.loginForm.patchValue({ emailOrUsername: 'david', password: 'WrongPass1!' });
    component.onSubmit();
    tick();

    expect(component.errorMessage).toBe('Identifiants invalides');
    expect(router.navigate).not.toHaveBeenCalled();
  }));

  // ─── Test 3 ─────────────────────────────────────────────────────────────────

  it('formulaire invalide (champs vides) — ne doit pas appeler authService.login()', () => {
    // Champs vides → Validators.required échoue
    component.loginForm.patchValue({ emailOrUsername: '', password: '' });

    component.onSubmit();

    expect(authService.login).not.toHaveBeenCalled();
  });
});

