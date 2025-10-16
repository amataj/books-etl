import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import BookFile from './book-file';
import BookFileDetail from './book-file-detail';
import BookFileUpdate from './book-file-update';
import BookFileDeleteDialog from './book-file-delete-dialog';

const BookFileRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<BookFile />} />
    <Route path="new" element={<BookFileUpdate />} />
    <Route path=":id">
      <Route index element={<BookFileDetail />} />
      <Route path="edit" element={<BookFileUpdate />} />
      <Route path="delete" element={<BookFileDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default BookFileRoutes;
