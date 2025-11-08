import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'booksEtlApp.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'book',
    data: { pageTitle: 'booksEtlApp.book.home.title' },
    loadChildren: () => import('./book/book.routes'),
  },
  {
    path: 'book-file',
    data: { pageTitle: 'booksEtlApp.bookFile.home.title' },
    loadChildren: () => import('./book-file/book-file.routes'),
  },
  {
    path: 'book-page-text',
    data: { pageTitle: 'booksEtlApp.bookPageText.home.title' },
    loadChildren: () => import('./book-page-text/book-page-text.routes'),
  },
  {
    path: 'ingest-run',
    data: { pageTitle: 'booksEtlApp.ingestRun.home.title' },
    loadChildren: () => import('./ingest-run/ingest-run.routes'),
  },
  {
    path: 'ingest-event',
    data: { pageTitle: 'booksEtlApp.ingestEvent.home.title' },
    loadChildren: () => import('./ingest-event/ingest-event.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
