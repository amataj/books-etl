import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Book from './book';
import BookFile from './book-file';
import BookPageText from './book-page-text';
import IngestRun from './ingest-run';
import IngestEvent from './ingest-event';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="book/*" element={<Book />} />
        <Route path="book-file/*" element={<BookFile />} />
        <Route path="book-page-text/*" element={<BookPageText />} />
        <Route path="ingest-run/*" element={<IngestRun />} />
        <Route path="ingest-event/*" element={<IngestEvent />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
