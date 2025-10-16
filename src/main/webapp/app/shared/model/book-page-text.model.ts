import { IBook } from 'app/shared/model/book.model';

export interface IBookPageText {
  id?: number;
  documentId?: string;
  pageNo?: number;
  text?: string | null;
  book?: IBook | null;
}

export const defaultValue: Readonly<IBookPageText> = {};
