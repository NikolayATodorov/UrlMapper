import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { UrlMappingService } from '../service/url-mapping.service';
import { IUrlMapping } from '../url-mapping.model';
import { UrlMappingFormService } from './url-mapping-form.service';

import { UrlMappingUpdateComponent } from './url-mapping-update.component';

describe('UrlMapping Management Update Component', () => {
  let comp: UrlMappingUpdateComponent;
  let fixture: ComponentFixture<UrlMappingUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let urlMappingFormService: UrlMappingFormService;
  let urlMappingService: UrlMappingService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, UrlMappingUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(UrlMappingUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(UrlMappingUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    urlMappingFormService = TestBed.inject(UrlMappingFormService);
    urlMappingService = TestBed.inject(UrlMappingService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const urlMapping: IUrlMapping = { id: 456 };

      activatedRoute.data = of({ urlMapping });
      comp.ngOnInit();

      expect(comp.urlMapping).toEqual(urlMapping);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUrlMapping>>();
      const urlMapping = { id: 123 };
      jest.spyOn(urlMappingFormService, 'getUrlMapping').mockReturnValue(urlMapping);
      jest.spyOn(urlMappingService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ urlMapping });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: urlMapping }));
      saveSubject.complete();

      // THEN
      expect(urlMappingFormService.getUrlMapping).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(urlMappingService.update).toHaveBeenCalledWith(expect.objectContaining(urlMapping));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUrlMapping>>();
      const urlMapping = { id: 123 };
      jest.spyOn(urlMappingFormService, 'getUrlMapping').mockReturnValue({ id: null });
      jest.spyOn(urlMappingService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ urlMapping: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: urlMapping }));
      saveSubject.complete();

      // THEN
      expect(urlMappingFormService.getUrlMapping).toHaveBeenCalled();
      expect(urlMappingService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUrlMapping>>();
      const urlMapping = { id: 123 };
      jest.spyOn(urlMappingService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ urlMapping });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(urlMappingService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
