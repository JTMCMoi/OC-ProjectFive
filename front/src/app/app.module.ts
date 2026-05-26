import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterModule } from '@angular/router';
import { ReactiveFormsModule } from '@angular/forms';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';

/* Routing */
import { AppRoutingModule } from './app-routing.module';

/* Interceptors */
import { JwtInterceptor } from './interceptors/jwt.interceptor';

/* Angular Material */
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';

/* Components */
import { AppComponent } from './app.component';
import { HomeComponent } from './pages/home/home.component';
import { RegisterComponent } from './features/auth/components/register/register.component';
import { LoginComponent } from './features/auth/components/login/login.component';
import { FeedComponent } from './pages/feed/feed.component';
import { NavbarComponent } from './features/navbar/navbar.component';
import { MeComponent } from './pages/me/me.component';
import { TopicsComponent } from './pages/topics/topics.component';
import { CommonModule } from '@angular/common';
import { CreatePostComponent } from './pages/createPost/create-post.component';
import { PostDetailComponent } from './pages/postDetail/post-detail.component';

@NgModule({
  declarations: [AppComponent, 
                 HomeComponent, 
                 LoginComponent, 
                 NavbarComponent,
                 MeComponent,
                 RegisterComponent,
                 FeedComponent, 
                 TopicsComponent,
                CreatePostComponent,
                PostDetailComponent],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    RouterModule,
    ReactiveFormsModule,
    HttpClientModule,
    MatButtonModule,
    MatIconModule,
    MatInputModule,
    MatFormFieldModule,
    CommonModule
  ],
  providers: [{
    provide: HTTP_INTERCEPTORS,
    useClass: JwtInterceptor,
    multi: true
  }],
  bootstrap: [AppComponent],
})
export class AppModule {}
