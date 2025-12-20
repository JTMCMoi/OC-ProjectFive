import { Component, OnInit } from '@angular/core';
import { PostApiService } from '../../post-api.service';
import { PostResponse } from 'src/app/shared/models/posts/post-response.model';

@Component({
  selector: 'app-feed',
  templateUrl: './feed.component.html',
  styleUrls: ['./feed.component.scss']
})
export class FeedComponent implements OnInit {
  posts: PostResponse[] = [];
  selectedSort: 'asc' | 'desc' = 'desc';

  constructor(private postService: PostApiService) {}

  ngOnInit(): void {
    this.postService.getAll().subscribe((data: PostResponse[]) => {
        this.posts = data;
      this.sortPosts();
    });
  }

  toggleSort() {
    this.selectedSort = this.selectedSort === 'asc' ? 'desc' : 'asc';
    this.sortPosts();
  }

  private sortPosts(): void {
    this.posts.sort((a, b) => {
      if (!a.createdAt) return 1;
      if (!b.createdAt) return -1;

      const dateA = new Date(a.createdAt).getTime();
      const dateB = new Date(b.createdAt).getTime();
      return this.selectedSort === 'asc' ? dateA - dateB : dateB - dateA;
    });
  }
}
