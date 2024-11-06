import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IUrlMapping } from '../url-mapping.model';
import { UrlMappingService } from '../service/url-mapping.service';

const urlMappingResolve = (route: ActivatedRouteSnapshot): Observable<null | IUrlMapping> => {
  const id = route.params['id'];
  if (id) {
    return inject(UrlMappingService)
      .find(id)
      .pipe(
        mergeMap((urlMapping: HttpResponse<IUrlMapping>) => {
          if (urlMapping.body) {
            return of(urlMapping.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default urlMappingResolve;
