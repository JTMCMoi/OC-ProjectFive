import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuard } from './core/auth/guards/auth.guard';

// consider a guard combined with canLoad / canActivate route option
// to manage unauthenticated user to access private routes
const routes: Routes = [

  // page publique
  { path: '', loadChildren: () => import('./features/home/home.module').then(m => m.HomeModule) },

  // module auth
  { path: 'auth', loadChildren: () => import('./features/auth/auth.module').then(m => m.AuthModule) },

  // modules protégés
  { path: 'posts', loadChildren: () => import('./features/posts/post.module').then(m => m.PostModule), canActivate: [AuthGuard] },
  { path: 'topics', loadChildren: () => import('./features/topics/topic.module').then(m => m.TopicsModule), canActivate: [AuthGuard] },
  { path: 'profile', loadChildren: () => import('./features/user/user.module').then(m => m.UserModule), canActivate: [AuthGuard] },

  // wildcard
  { path: '**', redirectTo: '/posts/feed' }
];
@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}