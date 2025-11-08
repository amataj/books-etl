import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import BookPageTextResolve from './route/book-page-text-routing-resolve.service';

const bookPageTextRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/book-page-text.component').then(m => m.BookPageTextComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/book-page-text-detail.component').then(m => m.BookPageTextDetailComponent),
    resolve: {
      bookPageText: BookPageTextResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/book-page-text-update.component').then(m => m.BookPageTextUpdateComponent),
    resolve: {
      bookPageText: BookPageTextResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/book-page-text-update.component').then(m => m.BookPageTextUpdateComponent),
    resolve: {
      bookPageText: BookPageTextResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default bookPageTextRoute;
