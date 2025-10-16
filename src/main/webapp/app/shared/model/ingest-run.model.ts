import dayjs from 'dayjs';

export interface IIngestRun {
  id?: number;
  startedAt?: dayjs.Dayjs | null;
  finishedAt?: dayjs.Dayjs | null;
  status?: string;
  filesSeen?: number | null;
  filesParsed?: number | null;
  filesFailed?: number | null;
}

export const defaultValue: Readonly<IIngestRun> = {};
