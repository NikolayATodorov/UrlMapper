import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { UrlMappingComponent } from './list/url-mapping.component';
import { UrlMappingDetailComponent } from './detail/url-mapping-detail.component';
import { UrlMappingUpdateComponent } from './update/url-mapping-update.component';
import UrlMappingResolve from './route/url-mapping-routing-resolve.service';

const urlMappingRoute: Routes = [
  {
    path: '',
    component: UrlMappingComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: UrlMappingDetailComponent,
    resolve: {
      urlMapping: UrlMappingResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: UrlMappingUpdateComponent,
    resolve: {
      urlMapping: UrlMappingResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: UrlMappingUpdateComponent,
    resolve: {
      urlMapping: UrlMappingResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default urlMappingRoute;
