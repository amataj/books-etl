import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import IngestRun from './ingest-run';
import IngestRunDetail from './ingest-run-detail';
import IngestRunUpdate from './ingest-run-update';
import IngestRunDeleteDialog from './ingest-run-delete-dialog';

const IngestRunRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<IngestRun />} />
    <Route path="new" element={<IngestRunUpdate />} />
    <Route path=":id">
      <Route index element={<IngestRunDetail />} />
      <Route path="edit" element={<IngestRunUpdate />} />
      <Route path="delete" element={<IngestRunDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default IngestRunRoutes;
