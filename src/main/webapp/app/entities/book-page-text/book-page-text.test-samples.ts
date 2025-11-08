import { IBookPageText, NewBookPageText } from './book-page-text.model';

export const sampleWithRequiredData: IBookPageText = {
  id: 11186,
  documentId: 'quirkilyXX',
  pageNo: 27830,
};

export const sampleWithPartialData: IBookPageText = {
  id: 29834,
  documentId: 'mehXXXXXXX',
  pageNo: 29611,
};

export const sampleWithFullData: IBookPageText = {
  id: 29109,
  documentId: 'worthXXXXX',
  pageNo: 14461,
  text: '../fake-data/blob/hipster.txt',
};

export const sampleWithNewData: NewBookPageText = {
  documentId: 'utterly reproachfully jeopardise',
  pageNo: 25401,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
