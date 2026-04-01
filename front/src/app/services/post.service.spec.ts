import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { PostService } from './post.service';
import { PostResponse, CommentResponse, PostRequest, CommentRequest } from '../models/post.model';

describe('PostService', () => {
  let service: PostService;
  let httpMock: HttpTestingController;
  const apiUrl = 'http://localhost:8080/api/posts';

  const mockComment: CommentResponse = {
    id: 1,
    content: 'Super article !',
    authorUsername: 'david',
    createdAt: new Date(),
  };

  const mockPost: PostResponse = {
    id: 1,
    title: 'Mon premier article',
    content: 'Contenu de l\'article',
    authorUsername: 'david',
    themeTitle: 'Java',
    themeId: 1,
    createdAt: new Date(),
    comments: [mockComment],
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [PostService],
    });
    service = TestBed.inject(PostService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => httpMock.verify());

  it('devrait être créé', () => {
    expect(service).toBeTruthy();
  });

  it('getFeed() — doit appeler GET /api/posts/feed', () => {
    service.getFeed().subscribe((posts) => {
      expect(posts.length).toBe(1);
      expect(posts[0].title).toBe('Mon premier article');
    });

    const req = httpMock.expectOne(`${apiUrl}/feed`);
    expect(req.request.method).toBe('GET');
    req.flush([mockPost]);
  });

  it('getPost() — doit appeler GET /api/posts/1 et retourner le post avec ses commentaires', () => {
    service.getPost(1).subscribe((post) => {
      expect(post.id).toBe(1);
      expect(post.comments.length).toBe(1);
      expect(post.comments[0].content).toBe('Super article !');
    });

    const req = httpMock.expectOne(`${apiUrl}/1`);
    expect(req.request.method).toBe('GET');
    req.flush(mockPost);
  });

  it('createPost() — doit appeler POST /api/posts avec les données', () => {
    const request: PostRequest = { themeId: 1, title: 'Nouveau titre', content: 'Contenu' };

    service.createPost(request).subscribe((post) => {
      expect(post.title).toBe('Mon premier article');
    });

    const req = httpMock.expectOne(apiUrl);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(request);
    req.flush(mockPost);
  });

  it('addComment() — doit appeler POST /api/posts/1/comments', () => {
    const commentReq: CommentRequest = { content: 'Super article !' };

    service.addComment(1, commentReq).subscribe((comment) => {
      expect(comment.content).toBe('Super article !');
      expect(comment.authorUsername).toBe('david');
    });

    const req = httpMock.expectOne(`${apiUrl}/1/comments`);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(commentReq);
    req.flush(mockComment);
  });
});

