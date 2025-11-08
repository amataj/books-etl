import { IBook } from 'app/entities/book/book.model';

export interface IBookPageText {
  id: number;
  documentId?: string | null;
  pageNo?: number | null;
  text?: string | null;
  book?: Pick<IBook, 'id' | 'documentId'> | null;
}

export type NewBookPageText = Omit<IBookPageText, 'id'> & { id: null };
