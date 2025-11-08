export interface IBook {
  id: number;
  documentId?: string | null;
  title?: string | null;
  author?: string | null;
  lang?: string | null;
  pages?: number | null;
}

export type NewBook = Omit<IBook, 'id'> & { id: null };
