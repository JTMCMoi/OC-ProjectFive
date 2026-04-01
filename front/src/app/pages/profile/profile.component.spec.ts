import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { of } from 'rxjs';
import { NO_ERRORS_SCHEMA } from '@angular/core';

import { ProfileComponent } from './profile.component';
import { AuthService } from 'src/app/services/auth.service';
import { UserService } from 'src/app/services/user.service';
import { ThemeService } from 'src/app/services/theme.service';

const mockUser = { id: 1, username: 'david', email: 'david@test.com' };

describe('ProfileComponent (intégration)', () => {
  let component: ProfileComponent;
  let fixture: ComponentFixture<ProfileComponent>;

  let userService: { getProfile: jest.Mock; updateProfile: jest.Mock };
  let authService: { logout: jest.Mock };
  let themeService: { getSubscribedThemes: jest.Mock; unsubscribe: jest.Mock };
  let router: { navigate: jest.Mock };

  beforeEach(async () => {
    userService  = { getProfile: jest.fn(() => of(mockUser)), updateProfile: jest.fn() };
    authService  = { logout: jest.fn() };
    themeService = { getSubscribedThemes: jest.fn(() => of([])), unsubscribe: jest.fn() };
    router       = { navigate: jest.fn() };

    await TestBed.configureTestingModule({
      declarations: [ProfileComponent],
      imports: [ReactiveFormsModule],
      providers: [
        { provide: UserService,  useValue: userService },
        { provide: AuthService,  useValue: authService },
        { provide: ThemeService, useValue: themeService },
        { provide: Router,       useValue: router },
      ],
      schemas: [NO_ERRORS_SCHEMA],
    }).compileComponents();

    fixture   = TestBed.createComponent(ProfileComponent);
    component = fixture.componentInstance;
    fixture.detectChanges(); // déclenche ngOnInit
  });

  // ─── Test 1 ─────────────────────────────────────────────────────────────────

  it('doit charger le profil courant au démarrage (ngOnInit)', () => {
    expect(userService.getProfile).toHaveBeenCalled();
    expect(component.currentUser).toEqual(mockUser);
    expect(component.profileForm.value.email).toBe(mockUser.email);
    expect(component.profileForm.value.username).toBe(mockUser.username);
  });

  // ─── Test 2 ─────────────────────────────────────────────────────────────────

  it('onSubmit() — après une mise à jour réussie, doit déconnecter et rediriger vers /login', fakeAsync(() => {
    userService.updateProfile.mockReturnValue(of(mockUser));

    // Modifie le username pour que la requête ne soit pas vide
    component.profileForm.patchValue({ username: 'nouveauNom' });
    component.onSubmit();
    tick();

    expect(userService.updateProfile).toHaveBeenCalledWith({ username: 'nouveauNom' });
    expect(authService.logout).toHaveBeenCalled();
    expect(router.navigate).toHaveBeenCalledWith(['/login']);
  }));

  // ─── Test 3 ─────────────────────────────────────────────────────────────────

  it('onSubmit() — si le formulaire est invalide, ne doit pas appeler updateProfile', () => {
    // Email mal formé → Validators.email échoue → form.invalid
    component.profileForm.patchValue({ email: 'pas-un-email' });

    component.onSubmit();

    expect(userService.updateProfile).not.toHaveBeenCalled();
    expect(authService.logout).not.toHaveBeenCalled();
  });
});


