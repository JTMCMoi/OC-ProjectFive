import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

/* Guards */
import { AuthGuard } from './guards/auth.guard';
import { UnauthGuard } from './guards/unauth.guard';

/* Components */
import { HomeComponent } from './pages/home/home.component';
import { RegisterComponent } from './features/auth/components/register/register.component';
import { LoginComponent } from './features/auth/components/login/login.component';
import { FeedComponent } from './pages/feed/feed.component';
import { MeComponent } from './pages/me/me.component';
import { TopicsComponent } from './pages/topics/topics.component';
import { CreatePostComponent } from './pages/createPost/create-post.component';
import { PostDetailComponent } from './pages/postDetail/post-detail.component';

const routes: Routes = [
  /* Page d'accueil*/ 
  { path: '', component: HomeComponent, canActivate: [UnauthGuard] },

  /* Routes publiques */ 
  { path: 'login', component: LoginComponent, canActivate: [UnauthGuard] },
  { path: 'register', component: RegisterComponent, canActivate: [UnauthGuard] },

  /* Routes sécurisées */
  { path: 'feed',    component: FeedComponent,    canActivate: [AuthGuard] },
  { path: 'topics',  component: TopicsComponent,  canActivate: [AuthGuard] },
  { path: 'me', component: MeComponent, canActivate: [AuthGuard] },
  { path: 'posts/create', component: CreatePostComponent, canActivate: [AuthGuard] },
  { path: 'posts/:id', component: PostDetailComponent, canActivate: [AuthGuard] } 
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
