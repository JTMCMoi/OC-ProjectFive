import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterTestingModule } from '@angular/router/testing';
import { of, throwError } from 'rxjs';
import { PostDetailComponent } from './post-detail.component';
import { PostService } from '../../services/post.service';
import { PostResponse, CommentResponse } from '../../models/post.model';
import { NO_ERRORS_SCHEMA } from '@angular/core';

const mockComment: CommentResponse = {
  id: 1,
  content: 'Super article !',
  authorUsername: 'david',
  createdAt: new Date(),
};

const mockPost: PostResponse = {
  id: 1,
  title: 'Mon premier article',
  content: 'Contenu de test',
  authorUsername: 'david',
  themeTitle: 'Java',
  themeId: 1,
  createdAt: new Date(),
  comments: [],
};

describe('PostDetailComponent', () => {
  let component: PostDetailComponent;
  let fixture: ComponentFixture<PostDetailComponent>;
  let postServiceMock: jest.Mocked<PostService>;

  beforeEach(async () => {
    postServiceMock = {
      getPost: jest.fn().mockReturnValue(of(mockPost)),
      addComment: jest.fn(),
    } as any;

    await TestBed.configureTestingModule({
      declarations: [PostDetailComponent],
      imports: [ReactiveFormsModule, RouterTestingModule],
      providers: [{ provide: PostService, useValue: postServiceMock }],
      schemas: [NO_ERRORS_SCHEMA], // ignore les composants Angular Material
    }).compileComponents();

    fixture = TestBed.createComponent(PostDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('devrait être créé', () => {
    expect(component).toBeTruthy();
  });

  it('doit charger le post au ngOnInit', () => {
    expect(postServiceMock.getPost).toHaveBeenCalled();
    expect(component.post).toEqual(mockPost);
    expect(component.loading).toBe(false);
  });

  it('le formulaire de commentaire est invalide quand le champ est vide', () => {
    component.commentForm.get('content')?.setValue('');
    expect(component.commentForm.invalid).toBe(true);
  });

  it('le formulaire de commentaire est valide quand le champ est rempli', () => {
    component.commentForm.get('content')?.setValue('Mon commentaire');
    expect(component.commentForm.valid).toBe(true);
  });

  it('submitComment() — ne soumet pas si le formulaire est invalide', () => {
    component.commentForm.get('content')?.setValue('');
    component.submitComment();
    expect(postServiceMock.addComment).not.toHaveBeenCalled();
  });

  it('submitComment() — ajoute le commentaire à la liste après succès', () => {
    postServiceMock.addComment.mockReturnValue(of(mockComment));
    component.commentForm.get('content')?.setValue('Super article !');

    component.submitComment();

    expect(postServiceMock.addComment).toHaveBeenCalledWith(1, { content: 'Super article !' });
    expect(component.post!.comments).toContain(mockComment);
  });

  it('doit afficher une erreur si le post est introuvable', () => {
    postServiceMock.getPost.mockReturnValue(throwError(() => new Error('Not found')));

    component.ngOnInit();

    expect(component.errorMessage).toBe('Article introuvable.');
  });
});

