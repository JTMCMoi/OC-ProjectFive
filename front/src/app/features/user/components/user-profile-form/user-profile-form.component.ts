import { Component, OnInit } from '@angular/core';
import { User } from 'src/app/shared/models/user/user.model';
import { UserApiService } from '../../services/user-api.service';
import { UserUpdate } from 'src/app/shared/models/user/user-update.model';
import { AuthService } from 'src/app/core/auth/services/auth.service';

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

  constructor( private userService: UserApiService, private authService: AuthService
  ) {}

ngOnInit(): void {
  this.userService.getCurrentUser().subscribe(user => {
    this.user = user;
    this.username = user.username;
    this.email = user.email;
    this.password = '';
    this.authService.updateCurrentUser(user);
  });

  this.authService.currentUser$.subscribe(user => {
    if (user) {
      this.user = user;
      this.username = user.username;
      this.email = user.email;
    }
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
        console.log("Profil mis à jour !");
        this.authService.updateCurrentUser(updated);
        this.password = '';
      },
      error: err => console.error(err)
    });
  }
}
