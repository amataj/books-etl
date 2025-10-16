import dayjs from 'dayjs';
import { IBook } from 'app/shared/model/book.model';

export interface IBookFile {
  id?: number;
  pathNorm?: string;
  sha256?: string;
  sizeBytes?: number | null;
  mtime?: dayjs.Dayjs | null;
  storageUri?: string | null;
  firstSeenAt?: dayjs.Dayjs | null;
  lastSeenAt?: dayjs.Dayjs | null;
  book?: IBook | null;
}

export const defaultValue: Readonly<IBookFile> = {};
