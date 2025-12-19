import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { TopicApiService } from 'src/app/features/topics/topic-api.service';
import { Topic } from 'src/app/shared/models/topics/topic.model';
import { PostApiService } from '../../post-api.service';
import { AuthService } from 'src/app/core/auth/services/auth.service';
import { User } from 'src/app/shared/models/user/user.model';
import { filter } from 'rxjs/operators';
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
  user!: User;

  constructor(
    private topicService: TopicApiService,
    private postService: PostApiService,
    private router: Router,
    private authService: AuthService // <-- ajout
  ) {}

  ngOnInit(): void {
    // 🔑 Récupération de l'utilisateur connecté
    this.authService.currentUser$
      .pipe(filter((user): user is User => !!user))
      .subscribe(user => this.user = user);

    this.topicService.getAll().subscribe(topics => this.topics = topics);
  }

  goBack(): void {
    history.back();
  }

 createPost(): void {
  if (!this.selectedTopicId || !this.title.trim() || !this.content.trim()) return;

  this.postService.create({
    topicId: this.selectedTopicId,
    title: this.title,
    content: this.content
  }).subscribe({
    next: () => this.router.navigate(['/feed']),
    error: (err) => alert('Erreur lors de la création de l’article : ' + err.message)
  });
}

}