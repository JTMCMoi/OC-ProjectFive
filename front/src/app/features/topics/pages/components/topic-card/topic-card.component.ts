import { Component, Input, Output, EventEmitter } from '@angular/core';
import { Topic } from 'src/app/shared/models/topics/topic.model';

@Component({
  selector: 'app-topic-card',
  templateUrl: './topic-card.component.html',
  styleUrls: ['./topic-card.component.scss'],
})
export class TopicCardComponent {

  @Input() topic!: Topic;
  @Input() isSubscribed!: boolean;

  // Mode profil : on montre le bouton "Se désabonner"
  @Input() showUnsubscribe = false;

  @Output() subscribe = new EventEmitter<void>();
  @Output() unsubscribe = new EventEmitter<void>();

  onSubscribe() {
    this.subscribe.emit();
  }

  onUnsubscribe() {
    this.unsubscribe.emit();
  }
}
