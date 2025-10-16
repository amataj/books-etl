import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import BookPageText from './book-page-text';
import BookPageTextDetail from './book-page-text-detail';
import BookPageTextUpdate from './book-page-text-update';
import BookPageTextDeleteDialog from './book-page-text-delete-dialog';

const BookPageTextRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<BookPageText />} />
    <Route path="new" element={<BookPageTextUpdate />} />
    <Route path=":id">
      <Route index element={<BookPageTextDetail />} />
      <Route path="edit" element={<BookPageTextUpdate />} />
      <Route path="delete" element={<BookPageTextDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default BookPageTextRoutes;
