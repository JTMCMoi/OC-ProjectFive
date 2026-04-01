import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { ThemeService } from './theme.service';
import { Theme } from '../models/theme.model';

describe('ThemeService', () => {
  let service: ThemeService;
  let httpMock: HttpTestingController;
  const apiUrl = 'http://localhost:8080/api/themes';

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [ThemeService],
    });
    service = TestBed.inject(ThemeService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => httpMock.verify());

  const mockThemes: Theme[] = [
    { id: 1, title: 'Java', description: 'Java programming', subscribed: false },
    { id: 2, title: 'Angular', description: 'Frontend framework', subscribed: true },
  ];

  it('devrait être créé', () => {
    expect(service).toBeTruthy();
  });

  it('getAllThemes() — doit appeler GET /api/themes et retourner les thèmes', () => {
    service.getAllThemes().subscribe((themes) => {
      expect(themes.length).toBe(2);
      expect(themes[0].title).toBe('Java');
    });

    const req = httpMock.expectOne(apiUrl);
    expect(req.request.method).toBe('GET');
    req.flush(mockThemes);
  });

  it('getSubscribedThemes() — doit appeler GET /api/themes/subscribed', () => {
    const subscribed = [mockThemes[1]];

    service.getSubscribedThemes().subscribe((themes) => {
      expect(themes.length).toBe(1);
      expect(themes[0].subscribed).toBe(true);
    });

    const req = httpMock.expectOne(`${apiUrl}/subscribed`);
    expect(req.request.method).toBe('GET');
    req.flush(subscribed);
  });

  it('subscribe() — doit appeler POST /api/themes/1/subscribe', () => {
    service.subscribe(1).subscribe();

    const req = httpMock.expectOne(`${apiUrl}/1/subscribe`);
    expect(req.request.method).toBe('POST');
    req.flush({ message: 'Abonnement effectué' });
  });

  it('unsubscribe() — doit appeler DELETE /api/themes/1/subscribe', () => {
    service.unsubscribe(1).subscribe();

    const req = httpMock.expectOne(`${apiUrl}/1/subscribe`);
    expect(req.request.method).toBe('DELETE');
    req.flush({ message: 'Désabonnement effectué' });
  });
});

