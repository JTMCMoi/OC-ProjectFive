import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { TopicApiService } from 'src/app/features/topics/topic-api.service';
import { Topic } from 'src/app/shared/models/topics/topic.model';
import { PostApiService } from '../../post-api.service';

@Component({
  selector: 'app-create-post',
  templateUrl: './create-post.component.html',
  styleUrls: ['./create-post.component.scss']
})
export class CreatePostComponent implements OnInit {
  topics: Topic[] = [];
  selectedTopicId!: number;
  title = '';
  content = '';

  constructor(
    private topicService: TopicApiService,
    private postservice: PostApiService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.topicService.getAll().subscribe(topics => this.topics = topics)
  }

  goBack(): void {
    history.back();
  }

  createPost(): void {
    if (!this.selectedTopicId || !this.title.trim() || !this.content.trim()) return;

    this.postservice.create({
      topicId: this.selectedTopicId,
      title: this.title,
      content: this.content
    }).subscribe(() => this.router.navigate(['/feed']));
  }
}