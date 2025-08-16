import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ArticleListComponent } from './article-list/article-list.component';
import { CreateArticleComponent } from './create-article/create-article.component';
import { AppLayoutComponent } from 'src/app/shared/app-layout/app-layout.component';
import { ArticleDetailComponent } from './article-detail/article-detail.component';

const routes: Routes = [
  {
    path: '', component: AppLayoutComponent,
    children: [
      {
        path: 'list', component: ArticleListComponent
      },
      {
        path: 'create', component: CreateArticleComponent
      },
      {
        path:':id', component: ArticleDetailComponent 
      },
      {
        path: '**', redirectTo: 'list' // Redirect to article list if no route matches
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ArticleRoutingModule { }
