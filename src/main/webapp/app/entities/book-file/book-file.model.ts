import dayjs from 'dayjs/esm';
import { IBook } from 'app/entities/book/book.model';

export interface IBookFile {
  id: number;
  pathNorm?: string | null;
  sha256?: string | null;
  sizeBytes?: number | null;
  mtime?: dayjs.Dayjs | null;
  storageUri?: string | null;
  firstSeenAt?: dayjs.Dayjs | null;
  lastSeenAt?: dayjs.Dayjs | null;
  book?: Pick<IBook, 'id' | 'documentId'> | null;
}

export type NewBookFile = Omit<IBookFile, 'id'> & { id: null };
