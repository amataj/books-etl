import dayjs from 'dayjs/esm';

import { IBookFile, NewBookFile } from './book-file.model';

export const sampleWithRequiredData: IBookFile = {
  id: 17568,
  pathNorm: '../fake-data/blob/hipster.txt',
  sha256: 'geez beside',
};

export const sampleWithPartialData: IBookFile = {
  id: 30122,
  pathNorm: '../fake-data/blob/hipster.txt',
  sha256: 'oof following',
  sizeBytes: 20447,
  storageUri: '../fake-data/blob/hipster.txt',
  lastSeenAt: dayjs('2025-11-07T15:27'),
};

export const sampleWithFullData: IBookFile = {
  id: 8412,
  pathNorm: '../fake-data/blob/hipster.txt',
  sha256: 'modulo orX',
  sizeBytes: 18358,
  mtime: dayjs('2025-11-07T14:38'),
  storageUri: '../fake-data/blob/hipster.txt',
  firstSeenAt: dayjs('2025-11-07T15:37'),
  lastSeenAt: dayjs('2025-11-07T04:14'),
};

export const sampleWithNewData: NewBookFile = {
  pathNorm: '../fake-data/blob/hipster.txt',
  sha256: 'fundraising untidy quizzically',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
