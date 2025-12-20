import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSelectModule } from '@angular/material/select';
import { MatOptionModule } from '@angular/material/core';

import { SharedModule } from '../../shared/shared.module';
import { CoreModule } from '../../core/core.module';

import { UserRoutingModule } from './user-routing.module';

import { UserProfileComponent } from './pages/user-profile/user-profile.component';
import { UserProfileFormComponent } from './components/user-profile-form/user-profile-form.component';
import { TopicsModule } from '../topics/topic.module';

@NgModule({
  declarations: [
    UserProfileComponent,
    UserProfileFormComponent,
  ],
  imports: [
    UserRoutingModule,
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    CoreModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatProgressSpinnerModule,
    MatSelectModule,
    MatOptionModule,
    TopicsModule,
    SharedModule
  ],
})
export class UserModule {}
