import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { PostApiService } from '../../post-api.service';
import { PostResponse } from 'src/app/shared/models/posts/post-response.model';
import { CommentResponse } from 'src/app/shared/models/comments/comment-response.model';
import { CommentApiService } from 'src/app/features/comments/comment-api-service';
import { CommentCreate } from 'src/app/shared/models/comments/comment-create-requesst.model';

@Component({
  selector: 'app-details-post',
  templateUrl: './details-post.component.html',
  styleUrls: ['./details-post.component.scss']
})
export class DetailsPostComponent implements OnInit {

  post!: PostResponse;
  postId!: number;

  comments: CommentResponse[] = [];
  newComment: string = "";

  constructor(
    private route: ActivatedRoute,
    private postService: PostApiService,
    private commentService: CommentApiService
  ) {}

  ngOnInit(): void {
    this.postId = Number(this.route.snapshot.params['id']);
    this.loadPost();
    this.loadComments();
  }

  loadPost() {
    this.postService.getById(this.postId).subscribe(post => {
      this.post = post;
    });
  }

  loadComments() {
    this.commentService.getByPost(this.postId).subscribe(data => {
      this.comments = data;
                            console.log("comments",this.comments)

    });
  }

  sendComment() {
    if (!this.newComment.trim()) return;

    const dto: CommentCreate = {
      content: this.newComment,
      postId: this.postId
    };

    this.commentService.create(dto).subscribe((created) => {
      this.comments.push({
        id: created.id,
        content: created.content,
        authorUsername: created.authorUsername,
        createdAt: created.createdAt
      });
      this.newComment = "";
} ) ;
  }

  deleteComment(id: number) {
    this.commentService.delete(id).subscribe(() => {
      this.comments = this.comments.filter(c => c.id !== id);
    });
  }
}
