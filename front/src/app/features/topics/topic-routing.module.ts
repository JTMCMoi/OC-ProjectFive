import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { TopicsComponent } from './pages/topics/topics.component';
import { AuthGuard } from 'src/app/core/auth/guards/auth.guard';


const routes: Routes = [
  {path: 'feed', component: TopicsComponent, canActivate: [AuthGuard]}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class TopicsRoutingModule { }