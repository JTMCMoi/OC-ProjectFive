import { Component, OnInit } from '@angular/core';
import { User } from 'src/app/shared/models/user/user.model';
import { UserApiService } from '../../services/user-api.service';
import { UserUpdate } from 'src/app/shared/models/user/user-update.model';

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

  constructor(private userService: UserApiService) {}

  ngOnInit(): void {
    this.loadUser();
  }

  loadUser(): void {
    // Exemple → à remplacer par ton vrai service API
    this.userService.getCurrentUser().subscribe(user => {
      this.user = user;
      this.username = user.username;
      this.email = user.email;
      this.password = ''; // On ne pré-remplit jamais un password
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
      },
      error: err => console.error(err)
    });
  }

}
