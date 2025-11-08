import { IBook, NewBook } from './book.model';

export const sampleWithRequiredData: IBook = {
  id: 3991,
  documentId: 'blue regal',
};

export const sampleWithPartialData: IBook = {
  id: 24837,
  documentId: 'gahXXXXXXX',
};

export const sampleWithFullData: IBook = {
  id: 8637,
  documentId: 'sniffXXXXX',
  title: 'however quicker',
  author: 'reasoning repeatedly instead',
  lang: 'blushing shipper',
  pages: 14923,
};

export const sampleWithNewData: NewBook = {
  documentId: 'taxicab nor',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
