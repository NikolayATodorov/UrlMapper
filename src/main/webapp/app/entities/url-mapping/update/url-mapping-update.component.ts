import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IUrlMapping } from '../url-mapping.model';
import { UrlMappingService } from '../service/url-mapping.service';
import { UrlMappingFormService, UrlMappingFormGroup } from './url-mapping-form.service';

@Component({
  standalone: true,
  selector: 'jhi-url-mapping-update',
  templateUrl: './url-mapping-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class UrlMappingUpdateComponent implements OnInit {
  isSaving = false;
  urlMapping: IUrlMapping | null = null;

  protected urlMappingService = inject(UrlMappingService);
  protected urlMappingFormService = inject(UrlMappingFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: UrlMappingFormGroup = this.urlMappingFormService.createUrlMappingFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ urlMapping }) => {
      this.urlMapping = urlMapping;
      if (urlMapping) {
        this.updateForm(urlMapping);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const urlMapping = this.urlMappingFormService.getUrlMapping(this.editForm);
    if (urlMapping.id !== null) {
      this.subscribeToSaveResponse(this.urlMappingService.update(urlMapping));
    } else {
      this.subscribeToSaveResponse(this.urlMappingService.create(urlMapping));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUrlMapping>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(urlMapping: IUrlMapping): void {
    this.urlMapping = urlMapping;
    this.urlMappingFormService.resetForm(this.editForm, urlMapping);
  }
}
