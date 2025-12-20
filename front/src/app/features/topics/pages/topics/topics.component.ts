import { Component, OnInit } from '@angular/core';
import { Topic } from 'src/app/shared/models/topics/topic.model';
import { SubscriptionResponse } from 'src/app/shared/models/subscriptions/subscription.model';
import { TopicApiService } from '../../topic-api.service';
import { SubscriptionApiService } from 'src/app/features/subscriptions/subscription-api.service';
import { AuthService } from 'src/app/core/auth/services/auth.service';
import { User } from 'src/app/shared/models/user/user.model';
import { filter } from 'rxjs/operators';

@Component({
  selector: 'app-topics',
  templateUrl: './topics.component.html',
  styleUrls: ['./topics.component.scss']
})
export class TopicsComponent implements OnInit {
  topics: Topic[] = [];
  subscriptions: SubscriptionResponse[] = [];
  loading = true;
  errorMsg = '';
  user!: User;

  constructor(
    private topicApi: TopicApiService,
    private subsApi: SubscriptionApiService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    // 🔑 Écoute les changements de l'utilisateur
    this.authService.currentUser$
      .pipe(filter((user): user is User => !!user))
      .subscribe(user => {
        this.user = user;
        this.loadData();
      });
  }

  private loadData(): void {
    this.loading = true;

    Promise.all([
      this.topicApi.getAll().toPromise(),
      this.subsApi.getMySubscriptions().toPromise()
    ])
      .then(([topics, subs]) => {
        this.topics = (topics ?? []).map(t => ({
          id: t.id,
          title: t.title,
          description: t.description,
          createdAt: t.createdAt
        }));

        this.subscriptions = subs ?? [];
        this.loading = false;
      })
      .catch(() => {
        this.errorMsg = 'Impossible de charger les thèmes.';
        this.loading = false;
      });
  }

  isSubscribed(topicId: number): boolean {
    return this.subscriptions.some(s => s.topicId === topicId);
  }

  onSubscribe(topic: Topic) {
    this.subsApi.subscribe(topic.id).subscribe({
      next: sub => this.subscriptions.push(sub),
      error: () => alert("Erreur lors de l’abonnement.")
    });
  }

  onUnsubscribe(topic: Topic) {
    this.subsApi.unsubscribe(topic.id).subscribe({
      next: () =>
        (this.subscriptions = this.subscriptions.filter(
          s => s.topicId !== topic.id
        )),
      error: () => alert("Erreur lors du désabonnement.")
    });
  }
}
