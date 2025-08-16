import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { RegisterOrUpdateRequest } from 'src/app/shared/models/registerRequest';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private userUrl = `${environment.apiUrl}/user`;

  constructor(private http: HttpClient) { }

  updateUserInfo(updateRequest: RegisterOrUpdateRequest){
    return this.http.put(`${this.userUrl}`, updateRequest, { withCredentials: true });
  }
}
