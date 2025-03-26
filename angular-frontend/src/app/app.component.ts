import { Component } from '@angular/core';
import { UploadFormComponent } from './components/upload-form/upload-form.component';

@Component({
  selector: 'app-root',
  standalone: true,
  template: '<app-upload-form></app-upload-form>',
  imports: [UploadFormComponent]
})
export class AppComponent {}
