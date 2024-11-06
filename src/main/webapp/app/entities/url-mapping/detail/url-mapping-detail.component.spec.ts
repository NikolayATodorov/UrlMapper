import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { UrlMappingDetailComponent } from './url-mapping-detail.component';

describe('UrlMapping Management Detail Component', () => {
  let comp: UrlMappingDetailComponent;
  let fixture: ComponentFixture<UrlMappingDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UrlMappingDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: UrlMappingDetailComponent,
              resolve: { urlMapping: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(UrlMappingDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(UrlMappingDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load urlMapping on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', UrlMappingDetailComponent);

      // THEN
      expect(instance.urlMapping()).toEqual(expect.objectContaining({ id: 123 }));
    });
  });

  describe('PreviousState', () => {
    it('Should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
