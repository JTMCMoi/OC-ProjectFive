import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './pages/home/home.component';
import { LoginComponent } from './pages/login/login.component';
import { RegisterComponent } from './pages/register/register.component';
import { AuthGuard } from './shared/guard/auth.guard';

// consider a guard combined with canLoad / canActivate route option
// to manage unauthenticated user to access private routes
const routes: Routes = [
  {
    path: '',
    children: [
      { path: '', component: HomeComponent },
      { path: 'login', component: LoginComponent },
      { path: 'register', component: RegisterComponent },
      {
        path: 'articles', canLoad: [AuthGuard],
        canActivate: [AuthGuard], loadChildren: () => import('./pages/article/article.module').then(m => m.ArticleModule)
      },
      {
        path: 'utilisateur', canLoad: [AuthGuard],
        canActivate: [AuthGuard], loadChildren: () => import('./pages/user/user.module').then(m => m.UserModule)
      }, // Lazy load UserModule
      {
        path: 'theme', canLoad: [AuthGuard],
        canActivate: [AuthGuard], loadChildren: () => import('./pages/theme/theme.module').then(m => m.ThemeModule)
      }, // Lazy load ThemeModule
      { path: '**', redirectTo: '' }, // Redirect to home if no route matches
    ]
  },


];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule { }
