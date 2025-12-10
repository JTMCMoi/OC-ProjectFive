import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { UserApiService } from 'src/app/features/user/services/user-api.service';
import { User } from 'src/app/shared/models/user/user.model';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private api: UserApiService) {}

  getCurrentUser(): Observable<User> {
    return this.api.getCurrentUser();
  }

  getById(id: number): Observable<User> {
    return this.api.getById(id);
  }

  deleteAccount(id: number): Observable<void> {
    return this.api.delete(id);
  }
}
