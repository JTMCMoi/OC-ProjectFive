import { Component, OnInit } from '@angular/core';
import { Topic } from 'src/app/shared/models/topics/topic.model';
import { TopicApiService } from 'src/app/features/topics/topic-api.service';
import { SubscriptionApiService } from 'src/app/features/subscriptions/subscription-api.service';

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.scss']
})
export class UserProfileComponent implements OnInit {

  userTopics: Topic[] = [];
  loading = true;
  errorMsg = '';

  constructor(private subsApi: SubscriptionApiService , private topicApi: TopicApiService ) {}

  ngOnInit(): void {
    this.loadUserTopics();
  }

private loadUserTopics(): void {
  this.loading = true;

  // 1) On charge d’abord les subscriptions
  this.subsApi.getMySubscriptions().subscribe({
    next: (subs) => {

      // 2) Ensuite on charge tous les topics
      this.topicApi.getAll().subscribe({
        next: (allTopics) => {

          // 3) On map chaque subscription vers un vrai Topic
          this.userTopics = subs.map(s => {
            const fullTopic = allTopics.find(t => t.id === s.topicId);

            return {
              id: s.topicId,
              title: fullTopic?.title ?? s.topicName,
              description: fullTopic?.description ?? 'Aucune description',
              createdAt: fullTopic?.createdAt ?? s.subscribedAt ?? new Date().toISOString()
            };
          });

          this.loading = false;
        },
        error: () => {
          this.errorMsg = 'Impossible de charger les thèmes.';
          this.loading = false;
        }
      });

    },
    error: () => {
      this.errorMsg = 'Impossible de charger les abonnements.';
      this.loading = false;
    }
  });
}


  onUnsubscribe(topic: Topic) {
    this.subsApi.unsubscribe(topic.id).subscribe({
      next: () => {
        this.userTopics = this.userTopics.filter(t => t.id !== topic.id);
      },
      error: () => alert('Erreur lors du désabonnement.')
    });
  }
}
