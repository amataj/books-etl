import dayjs from 'dayjs';
import { IIngestRun } from 'app/shared/model/ingest-run.model';

export interface IIngestEvent {
  id?: number;
  runId?: string | null;
  documentId?: string | null;
  topic?: string;
  payload?: string;
  createdAt?: dayjs.Dayjs | null;
  ingestRun?: IIngestRun | null;
}

export const defaultValue: Readonly<IIngestEvent> = {};
