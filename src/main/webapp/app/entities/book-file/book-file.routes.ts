import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import BookFileResolve from './route/book-file-routing-resolve.service';

const bookFileRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/book-file.component').then(m => m.BookFileComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/book-file-detail.component').then(m => m.BookFileDetailComponent),
    resolve: {
      bookFile: BookFileResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/book-file-update.component').then(m => m.BookFileUpdateComponent),
    resolve: {
      bookFile: BookFileResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/book-file-update.component').then(m => m.BookFileUpdateComponent),
    resolve: {
      bookFile: BookFileResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default bookFileRoute;
