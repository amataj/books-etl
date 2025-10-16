export interface IBook {
  id?: number;
  documentId?: string;
  title?: string | null;
  author?: string | null;
  lang?: string | null;
  pages?: number | null;
}

export const defaultValue: Readonly<IBook> = {};
