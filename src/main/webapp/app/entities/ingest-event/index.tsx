import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import IngestEvent from './ingest-event';
import IngestEventDetail from './ingest-event-detail';
import IngestEventUpdate from './ingest-event-update';
import IngestEventDeleteDialog from './ingest-event-delete-dialog';

const IngestEventRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<IngestEvent />} />
    <Route path="new" element={<IngestEventUpdate />} />
    <Route path=":id">
      <Route index element={<IngestEventDetail />} />
      <Route path="edit" element={<IngestEventUpdate />} />
      <Route path="delete" element={<IngestEventDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default IngestEventRoutes;
