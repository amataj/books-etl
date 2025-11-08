import dayjs from 'dayjs/esm';

export interface IIngestRun {
  id: number;
  startedAt?: dayjs.Dayjs | null;
  finishedAt?: dayjs.Dayjs | null;
  status?: string | null;
  filesSeen?: number | null;
  filesParsed?: number | null;
  filesFailed?: number | null;
}

export type NewIngestRun = Omit<IIngestRun, 'id'> & { id: null };
