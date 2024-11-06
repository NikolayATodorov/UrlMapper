import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../url-mapping.test-samples';

import { UrlMappingFormService } from './url-mapping-form.service';

describe('UrlMapping Form Service', () => {
  let service: UrlMappingFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UrlMappingFormService);
  });

  describe('Service methods', () => {
    describe('createUrlMappingFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createUrlMappingFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            originalUrl: expect.any(Object),
            ttl: expect.any(Object),
            creationTimestamp: expect.any(Object),
          }),
        );
      });

      it('passing IUrlMapping should create a new form with FormGroup', () => {
        const formGroup = service.createUrlMappingFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            originalUrl: expect.any(Object),
            ttl: expect.any(Object),
            creationTimestamp: expect.any(Object),
          }),
        );
      });
    });

    describe('getUrlMapping', () => {
      it('should return NewUrlMapping for default UrlMapping initial value', () => {
        const formGroup = service.createUrlMappingFormGroup(sampleWithNewData);

        const urlMapping = service.getUrlMapping(formGroup) as any;

        expect(urlMapping).toMatchObject(sampleWithNewData);
      });

      it('should return NewUrlMapping for empty UrlMapping initial value', () => {
        const formGroup = service.createUrlMappingFormGroup();

        const urlMapping = service.getUrlMapping(formGroup) as any;

        expect(urlMapping).toMatchObject({});
      });

      it('should return IUrlMapping', () => {
        const formGroup = service.createUrlMappingFormGroup(sampleWithRequiredData);

        const urlMapping = service.getUrlMapping(formGroup) as any;

        expect(urlMapping).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IUrlMapping should not enable id FormControl', () => {
        const formGroup = service.createUrlMappingFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewUrlMapping should disable id FormControl', () => {
        const formGroup = service.createUrlMappingFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
