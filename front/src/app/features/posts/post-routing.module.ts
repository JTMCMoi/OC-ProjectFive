import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { FeedComponent } from './pages/feed/feed.component';
import { DetailsPostComponent } from './pages/details-post/details-post.component';
import { CreatePostComponent } from './pages/create-post/create-post.component';
import { AuthGuard } from 'src/app/core/auth/guards/auth.guard';


const routes: Routes = [
    { path: 'feed', component: FeedComponent, canActivate: [AuthGuard]},
    { path: 'create-post', component: CreatePostComponent, canActivate: [AuthGuard]},
    { path: 'details-post/:id', component: DetailsPostComponent, canActivate: [AuthGuard] },
];


@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class PostRoutingModule { }