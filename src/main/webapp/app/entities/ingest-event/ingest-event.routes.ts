import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import IngestEventResolve from './route/ingest-event-routing-resolve.service';

const ingestEventRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/ingest-event.component').then(m => m.IngestEventComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/ingest-event-detail.component').then(m => m.IngestEventDetailComponent),
    resolve: {
      ingestEvent: IngestEventResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/ingest-event-update.component').then(m => m.IngestEventUpdateComponent),
    resolve: {
      ingestEvent: IngestEventResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/ingest-event-update.component').then(m => m.IngestEventUpdateComponent),
    resolve: {
      ingestEvent: IngestEventResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default ingestEventRoute;
