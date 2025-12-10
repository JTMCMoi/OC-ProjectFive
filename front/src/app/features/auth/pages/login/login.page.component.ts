import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/core/auth/services/auth.service';

@Component({
  selector: 'app-login-page',
  templateUrl: './login.page.component.html',
  styleUrls: ['./login.page.component.scss']
})
export class LoginPageComponent {
  form: FormGroup;
  loading = false;
  error: string | null = null;

  constructor(private fb: FormBuilder, private auth: AuthService, private router: Router) {
    this.form = this.fb.group({
      usernameOrEmail: ['', [Validators.required]],
      password: ['', [Validators.required]]
    });
  }

  submit() {
    if (this.form.invalid) return;

    this.loading = true;
    this.error = null;

    this.auth.login(this.form.value).subscribe({
      next: () => {
        this.loading = false;
        this.router.navigate(['/posts/feed']);
      },
      error: (err) => {
        this.loading = false;
        this.error = err?.error?.message || 'Erreur d’authentification';
      }
    });
  }
}
