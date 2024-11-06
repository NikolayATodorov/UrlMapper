import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IUrlMapping, NewUrlMapping } from '../url-mapping.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IUrlMapping for edit and NewUrlMappingFormGroupInput for create.
 */
type UrlMappingFormGroupInput = IUrlMapping | PartialWithRequiredKeyOf<NewUrlMapping>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IUrlMapping | NewUrlMapping> = Omit<T, 'creationTimestamp'> & {
  creationTimestamp?: string | null;
};

type UrlMappingFormRawValue = FormValueOf<IUrlMapping>;

type NewUrlMappingFormRawValue = FormValueOf<NewUrlMapping>;

type UrlMappingFormDefaults = Pick<NewUrlMapping, 'id' | 'creationTimestamp'>;

type UrlMappingFormGroupContent = {
  id: FormControl<UrlMappingFormRawValue['id'] | NewUrlMapping['id']>;
  originalUrl: FormControl<UrlMappingFormRawValue['originalUrl']>;
  ttl: FormControl<UrlMappingFormRawValue['ttl']>;
  creationTimestamp: FormControl<UrlMappingFormRawValue['creationTimestamp']>;
};

export type UrlMappingFormGroup = FormGroup<UrlMappingFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class UrlMappingFormService {
  createUrlMappingFormGroup(urlMapping: UrlMappingFormGroupInput = { id: null }): UrlMappingFormGroup {
    const urlMappingRawValue = this.convertUrlMappingToUrlMappingRawValue({
      ...this.getFormDefaults(),
      ...urlMapping,
    });
    return new FormGroup<UrlMappingFormGroupContent>({
      id: new FormControl(
        { value: urlMappingRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      originalUrl: new FormControl(urlMappingRawValue.originalUrl, {
        validators: [Validators.required],
      }),
      ttl: new FormControl(urlMappingRawValue.ttl),
      creationTimestamp: new FormControl(urlMappingRawValue.creationTimestamp, {
        validators: [Validators.required],
      }),
    });
  }

  getUrlMapping(form: UrlMappingFormGroup): IUrlMapping | NewUrlMapping {
    return this.convertUrlMappingRawValueToUrlMapping(form.getRawValue() as UrlMappingFormRawValue | NewUrlMappingFormRawValue);
  }

  resetForm(form: UrlMappingFormGroup, urlMapping: UrlMappingFormGroupInput): void {
    const urlMappingRawValue = this.convertUrlMappingToUrlMappingRawValue({ ...this.getFormDefaults(), ...urlMapping });
    form.reset(
      {
        ...urlMappingRawValue,
        id: { value: urlMappingRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): UrlMappingFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      creationTimestamp: currentTime,
    };
  }

  private convertUrlMappingRawValueToUrlMapping(
    rawUrlMapping: UrlMappingFormRawValue | NewUrlMappingFormRawValue,
  ): IUrlMapping | NewUrlMapping {
    return {
      ...rawUrlMapping,
      creationTimestamp: dayjs(rawUrlMapping.creationTimestamp, DATE_TIME_FORMAT),
    };
  }

  private convertUrlMappingToUrlMappingRawValue(
    urlMapping: IUrlMapping | (Partial<NewUrlMapping> & UrlMappingFormDefaults),
  ): UrlMappingFormRawValue | PartialWithRequiredKeyOf<NewUrlMappingFormRawValue> {
    return {
      ...urlMapping,
      creationTimestamp: urlMapping.creationTimestamp ? urlMapping.creationTimestamp.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
