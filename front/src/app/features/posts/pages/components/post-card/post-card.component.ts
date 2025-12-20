import { Component, Input } from '@angular/core';
import { PostResponse } from 'src/app/shared/models/posts/post-response.model';

@Component({
  selector: 'app-post-card',
  templateUrl: './post-card.component.html',
  styleUrls: ['./post-card.component.scss']
})
export class PostCardComponent {
  @Input() post!: PostResponse;
  @Input() showTopic: boolean = false;
  @Input() variant: 'feed'| 'details' = 'feed';

}
