import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { AuthGuard } from 'src/app/core/auth/guards/auth.guard';
import { UserProfileComponent } from './pages/user-profile/user-profile.component';


const routes: Routes = [
    { path: 'me', component: UserProfileComponent, canActivate: [AuthGuard]},
];


@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class UserRoutingModule { }
