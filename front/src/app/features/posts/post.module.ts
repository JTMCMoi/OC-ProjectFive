import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatIconModule } from '@angular/material/icon';
import { MatSelectModule } from '@angular/material/select';
import { MatOptionModule } from '@angular/material/core';
import { MatGridListModule } from '@angular/material/grid-list';




import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { SharedModule } from '../../shared/shared.module';
import { CoreModule } from '../../core/core.module';
import { FeedComponent } from './pages/feed/feed.component';
import { CreatePostComponent } from './pages/create-post/create-post.component';
import { DetailsPostComponent } from './pages/details-post/details-post.component';
import { PostRoutingModule } from './post-routing.module';
import { PostCardComponent } from './pages/components/post-card/post-card.component';


@NgModule({
  declarations: [
    FeedComponent,
    CreatePostComponent,
    DetailsPostComponent,
    PostCardComponent
  ],
  imports: [
    PostRoutingModule,
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    CoreModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatProgressSpinnerModule,
    MatIconModule,
    MatSelectModule,  
    MatOptionModule,
    MatGridListModule,
    SharedModule
  ]
})
export class PostModule {}
