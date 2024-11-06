import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IUrlMapping } from '../url-mapping.model';

@Component({
  standalone: true,
  selector: 'jhi-url-mapping-detail',
  templateUrl: './url-mapping-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class UrlMappingDetailComponent {
  urlMapping = input<IUrlMapping | null>(null);

  previousState(): void {
    window.history.back();
  }
}
