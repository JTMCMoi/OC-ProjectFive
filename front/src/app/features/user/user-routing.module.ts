import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginPageComponent } from '../auth/pages/login/login.page.component';
import { RegisterPageComponent } from '../auth/pages/register/register.page.component';


const routes: Routes = [
  { title: 'Login', path: 'login', component: LoginPageComponent },
  { title: 'Register', path: 'register', component: RegisterPageComponent }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AuthRoutingModule { }