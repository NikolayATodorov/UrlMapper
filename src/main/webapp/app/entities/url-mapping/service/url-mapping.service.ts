import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { map, Observable } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IUrlMapping, NewUrlMapping } from '../url-mapping.model';

export type PartialUpdateUrlMapping = Partial<IUrlMapping> & Pick<IUrlMapping, 'id'>;

type RestOf<T extends IUrlMapping | NewUrlMapping> = Omit<T, 'creationTimestamp'> & {
  creationTimestamp?: string | null;
};

export type RestUrlMapping = RestOf<IUrlMapping>;

export type NewRestUrlMapping = RestOf<NewUrlMapping>;

export type PartialUpdateRestUrlMapping = RestOf<PartialUpdateUrlMapping>;

export type EntityResponseType = HttpResponse<IUrlMapping>;
export type EntityArrayResponseType = HttpResponse<IUrlMapping[]>;

@Injectable({ providedIn: 'root' })
export class UrlMappingService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/url-mappings');

  create(urlMapping: NewUrlMapping): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(urlMapping);
    return this.http
      .post<RestUrlMapping>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(urlMapping: IUrlMapping): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(urlMapping);
    return this.http
      .put<RestUrlMapping>(`${this.resourceUrl}/${this.getUrlMappingIdentifier(urlMapping)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(urlMapping: PartialUpdateUrlMapping): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(urlMapping);
    return this.http
      .patch<RestUrlMapping>(`${this.resourceUrl}/${this.getUrlMappingIdentifier(urlMapping)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestUrlMapping>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestUrlMapping[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getUrlMappingIdentifier(urlMapping: Pick<IUrlMapping, 'id'>): number {
    return urlMapping.id;
  }

  compareUrlMapping(o1: Pick<IUrlMapping, 'id'> | null, o2: Pick<IUrlMapping, 'id'> | null): boolean {
    return o1 && o2 ? this.getUrlMappingIdentifier(o1) === this.getUrlMappingIdentifier(o2) : o1 === o2;
  }

  addUrlMappingToCollectionIfMissing<Type extends Pick<IUrlMapping, 'id'>>(
    urlMappingCollection: Type[],
    ...urlMappingsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const urlMappings: Type[] = urlMappingsToCheck.filter(isPresent);
    if (urlMappings.length > 0) {
      const urlMappingCollectionIdentifiers = urlMappingCollection.map(urlMappingItem => this.getUrlMappingIdentifier(urlMappingItem));
      const urlMappingsToAdd = urlMappings.filter(urlMappingItem => {
        const urlMappingIdentifier = this.getUrlMappingIdentifier(urlMappingItem);
        if (urlMappingCollectionIdentifiers.includes(urlMappingIdentifier)) {
          return false;
        }
        urlMappingCollectionIdentifiers.push(urlMappingIdentifier);
        return true;
      });
      return [...urlMappingsToAdd, ...urlMappingCollection];
    }
    return urlMappingCollection;
  }

  protected convertDateFromClient<T extends IUrlMapping | NewUrlMapping | PartialUpdateUrlMapping>(urlMapping: T): RestOf<T> {
    return {
      ...urlMapping,
      creationTimestamp: urlMapping.creationTimestamp?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restUrlMapping: RestUrlMapping): IUrlMapping {
    return {
      ...restUrlMapping,
      creationTimestamp: restUrlMapping.creationTimestamp ? dayjs(restUrlMapping.creationTimestamp) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestUrlMapping>): HttpResponse<IUrlMapping> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestUrlMapping[]>): HttpResponse<IUrlMapping[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
