import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import BooksEtlKafka from './books-etl-kafka';

const BooksEtlKafkaRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<BooksEtlKafka />} />
  </ErrorBoundaryRoutes>
);

export default BooksEtlKafkaRoutes;
