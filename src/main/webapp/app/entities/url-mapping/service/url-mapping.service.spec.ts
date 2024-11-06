import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IUrlMapping } from '../url-mapping.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../url-mapping.test-samples';

import { UrlMappingService, RestUrlMapping } from './url-mapping.service';

const requireRestSample: RestUrlMapping = {
  ...sampleWithRequiredData,
  creationTimestamp: sampleWithRequiredData.creationTimestamp?.toJSON(),
};

describe('UrlMapping Service', () => {
  let service: UrlMappingService;
  let httpMock: HttpTestingController;
  let expectedResult: IUrlMapping | IUrlMapping[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(UrlMappingService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a UrlMapping', () => {
      const urlMapping = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(urlMapping).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a UrlMapping', () => {
      const urlMapping = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(urlMapping).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a UrlMapping', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of UrlMapping', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a UrlMapping', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addUrlMappingToCollectionIfMissing', () => {
      it('should add a UrlMapping to an empty array', () => {
        const urlMapping: IUrlMapping = sampleWithRequiredData;
        expectedResult = service.addUrlMappingToCollectionIfMissing([], urlMapping);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(urlMapping);
      });

      it('should not add a UrlMapping to an array that contains it', () => {
        const urlMapping: IUrlMapping = sampleWithRequiredData;
        const urlMappingCollection: IUrlMapping[] = [
          {
            ...urlMapping,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addUrlMappingToCollectionIfMissing(urlMappingCollection, urlMapping);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a UrlMapping to an array that doesn't contain it", () => {
        const urlMapping: IUrlMapping = sampleWithRequiredData;
        const urlMappingCollection: IUrlMapping[] = [sampleWithPartialData];
        expectedResult = service.addUrlMappingToCollectionIfMissing(urlMappingCollection, urlMapping);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(urlMapping);
      });

      it('should add only unique UrlMapping to an array', () => {
        const urlMappingArray: IUrlMapping[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const urlMappingCollection: IUrlMapping[] = [sampleWithRequiredData];
        expectedResult = service.addUrlMappingToCollectionIfMissing(urlMappingCollection, ...urlMappingArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const urlMapping: IUrlMapping = sampleWithRequiredData;
        const urlMapping2: IUrlMapping = sampleWithPartialData;
        expectedResult = service.addUrlMappingToCollectionIfMissing([], urlMapping, urlMapping2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(urlMapping);
        expect(expectedResult).toContain(urlMapping2);
      });

      it('should accept null and undefined values', () => {
        const urlMapping: IUrlMapping = sampleWithRequiredData;
        expectedResult = service.addUrlMappingToCollectionIfMissing([], null, urlMapping, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(urlMapping);
      });

      it('should return initial array if no UrlMapping is added', () => {
        const urlMappingCollection: IUrlMapping[] = [sampleWithRequiredData];
        expectedResult = service.addUrlMappingToCollectionIfMissing(urlMappingCollection, undefined, null);
        expect(expectedResult).toEqual(urlMappingCollection);
      });
    });

    describe('compareUrlMapping', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareUrlMapping(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareUrlMapping(entity1, entity2);
        const compareResult2 = service.compareUrlMapping(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareUrlMapping(entity1, entity2);
        const compareResult2 = service.compareUrlMapping(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareUrlMapping(entity1, entity2);
        const compareResult2 = service.compareUrlMapping(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
