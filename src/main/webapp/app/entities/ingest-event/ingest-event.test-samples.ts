import dayjs from 'dayjs/esm';

import { IIngestEvent, NewIngestEvent } from './ingest-event.model';

export const sampleWithRequiredData: IIngestEvent = {
  id: 12102,
  topic: 'which via',
  payload: '../fake-data/blob/hipster.txt',
};

export const sampleWithPartialData: IIngestEvent = {
  id: 16910,
  documentId: 'tool than yahoo',
  topic: 'lawmaker whoa clone',
  payload: '../fake-data/blob/hipster.txt',
};

export const sampleWithFullData: IIngestEvent = {
  id: 10346,
  runId: 'feed383d-ba2e-412e-bb9c-ac2d80b1bef7',
  documentId: 'like bleakly kindheartedly',
  topic: 'whenever event polarisation',
  payload: '../fake-data/blob/hipster.txt',
  createdAt: dayjs('2025-11-07T04:18'),
};

export const sampleWithNewData: NewIngestEvent = {
  topic: 'inside',
  payload: '../fake-data/blob/hipster.txt',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
