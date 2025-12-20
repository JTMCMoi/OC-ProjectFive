import { Component, OnInit } from '@angular/core';
import { User } from 'src/app/shared/models/user/user.model';
import { UserApiService } from '../../services/user-api.service';
import { UserUpdate } from 'src/app/shared/models/user/user-update.model';
import { AuthService } from 'src/app/core/auth/services/auth.service';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-user-profile-form',
  templateUrl: './user-profile-form.component.html',
  styleUrls: ['./user-profile-form.component.scss']
})
export class UserProfileFormComponent implements OnInit {

  user!: User;

  username = '';
  email = '';
  password = '';

  constructor(
    private userService: UserApiService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {

  this.authService.currentUser$.subscribe(user => {
    if (!user) return;

    this.user = user;
    this.username = user.username;
    this.email = user.email;
    this.password = '';
  });
}

  selectAll(event: Event) {
    const input = event.target as HTMLInputElement | HTMLTextAreaElement;
    setTimeout(() => input.select(), 0);
  }

 updateUser(): void {
  const payload: UserUpdate = {
    username: this.username,
    email: this.email,
    password: this.password?.trim() ? this.password : undefined
  };

  this.userService.updateMe(payload).subscribe({
    next: updated => {
      this.authService.setCurrentUser(updated);
      this.password = '';
    },
    error: (err: HttpErrorResponse) => {
      console.error(err);
    }
  });
}
}