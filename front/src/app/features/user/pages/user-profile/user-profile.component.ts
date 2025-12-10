import { Component, OnInit } from '@angular/core';
import { Topic } from 'src/app/shared/models/topics/topic.model';
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

  constructor(private subsApi: SubscriptionApiService) {}

  ngOnInit(): void {
    this.loadUserTopics();
  }

  private loadUserTopics(): void {
    this.loading = true;
    this.subsApi.getMySubscriptions().subscribe({
      next: (subs) => {
        // Chaque subscription contient topicId et topicName, on map vers Topic
        this.userTopics = subs.map(s => ({
  id: s.topicId,
  title: s.topicName,
  description: '',
  createdAt: s.subscribedAt ?? new Date().toISOString()
}));

        this.loading = false;
      },
      error: () => {
        this.errorMsg = 'Impossible de charger les thèmes abonnés';
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
