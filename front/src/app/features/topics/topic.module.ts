import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { TopicsComponent } from './pages/topics/topics.component';
import { TopicsRoutingModule } from './topic-routing.module';
import { TopicCardComponent } from './pages/components/topic-card/topic-card.component';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatIconModule } from '@angular/material/icon'

@NgModule({
  declarations: [
    TopicsComponent,
    TopicCardComponent
  ],
  imports: [
    CommonModule,
    HttpClientModule,
    TopicsRoutingModule,
    MatCardModule,
    MatButtonModule,
    MatProgressSpinnerModule,
    MatIconModule
  ],
  exports: [
    TopicCardComponent 
  ]
})
export class TopicsModule { }
