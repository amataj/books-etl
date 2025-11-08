import dayjs from 'dayjs/esm';

import { IIngestRun, NewIngestRun } from './ingest-run.model';

export const sampleWithRequiredData: IIngestRun = {
  id: 30198,
  status: 'equally until',
};

export const sampleWithPartialData: IIngestRun = {
  id: 1585,
  finishedAt: dayjs('2025-11-08T02:18'),
  status: 'overheard imagineer',
  filesFailed: 16485,
};

export const sampleWithFullData: IIngestRun = {
  id: 327,
  startedAt: dayjs('2025-11-07T08:27'),
  finishedAt: dayjs('2025-11-07T23:45'),
  status: 'mob',
  filesSeen: 23332,
  filesParsed: 26063,
  filesFailed: 1998,
};

export const sampleWithNewData: NewIngestRun = {
  status: 'fatally',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
