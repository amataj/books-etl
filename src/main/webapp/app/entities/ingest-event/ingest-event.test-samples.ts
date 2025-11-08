import dayjs from 'dayjs/esm';

import { IIngestEvent, NewIngestEvent } from './ingest-event.model';

const rawIngestEventPayload = JSON.stringify({
  event_id: '1f6cc052-79f9-4c5c-9f65-ea8f0d7f7a3d',
  discovered_at: '2025-10-15T10:00:00Z',
  path: 'file:///Books/foo/Bar.pdf',
  size_bytes: 123456,
  sha256: 'f4d5f160a2b9c8deabf4d2e8bb2c9a8cd1234567890abcdef1234567890abcd',
  source_host: 'ingest-host-01',
});

const parsedIngestEventPayload = JSON.stringify({
  event_id: '5b9e97b8-d52a-4a36-94fa-9e2d0cd4d29c',
  document_id: 'f4d5f160a2b9c8deabf4d2e8bb2c9a8cd1234567890abcdef1234567890abcd',
  title_guess: 'Clean Architecture',
  author_guess: 'Robert C. Martin',
  pages: 432,
  text_bytes: 3456789,
  lang: 'en',
  parser: 'pdfbox',
  parse_warnings: [] as string[],
});

export const sampleWithRequiredData: IIngestEvent = {
  id: 12102,
  topic: 'pdf.ingest.raw',
  payload: rawIngestEventPayload,
};

export const sampleWithPartialData: IIngestEvent = {
  id: 16910,
  documentId: 'f4d5f160a2b9c8deabf4d2e8bb2c9a8cd1234567890abcdef1234567890abcd',
  topic: 'pdf.ingest.parsed',
  payload: parsedIngestEventPayload,
};

export const sampleWithFullData: IIngestEvent = {
  id: 10346,
  runId: 'feed383d-ba2e-412e-bb9c-ac2d80b1bef7',
  documentId: '0c5e68b9dcb4a53c98f68fd1a2c47e3b11223344556677889900aabbccddeeff',
  topic: 'pdf.ingest.parsed',
  payload: parsedIngestEventPayload,
  createdAt: dayjs('2025-11-07T04:18'),
};

export const sampleWithNewData: NewIngestEvent = {
  topic: 'pdf.ingest.raw',
  payload: rawIngestEventPayload,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
