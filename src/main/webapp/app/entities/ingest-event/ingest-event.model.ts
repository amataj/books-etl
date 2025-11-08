import dayjs from 'dayjs/esm';
import { IIngestRun } from 'app/entities/ingest-run/ingest-run.model';

export interface IIngestEvent {
  id: number;
  runId?: string | null;
  documentId?: string | null;
  topic?: string | null;
  payload?: string | null;
  createdAt?: dayjs.Dayjs | null;
  ingestRun?: Pick<IIngestRun, 'id'> | null;
}

export type NewIngestEvent = Omit<IIngestEvent, 'id'> & { id: null };
