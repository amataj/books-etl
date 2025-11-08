import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import IngestRunResolve from './route/ingest-run-routing-resolve.service';

const ingestRunRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/ingest-run.component').then(m => m.IngestRunComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/ingest-run-detail.component').then(m => m.IngestRunDetailComponent),
    resolve: {
      ingestRun: IngestRunResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/ingest-run-update.component').then(m => m.IngestRunUpdateComponent),
    resolve: {
      ingestRun: IngestRunResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/ingest-run-update.component').then(m => m.IngestRunUpdateComponent),
    resolve: {
      ingestRun: IngestRunResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default ingestRunRoute;
