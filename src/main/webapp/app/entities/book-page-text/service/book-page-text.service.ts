import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBookPageText, NewBookPageText } from '../book-page-text.model';

export type PartialUpdateBookPageText = Partial<IBookPageText> & Pick<IBookPageText, 'id'>;

export type EntityResponseType = HttpResponse<IBookPageText>;
export type EntityArrayResponseType = HttpResponse<IBookPageText[]>;

@Injectable({ providedIn: 'root' })
export class BookPageTextService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/book-page-texts');

  create(bookPageText: NewBookPageText): Observable<EntityResponseType> {
    return this.http.post<IBookPageText>(this.resourceUrl, bookPageText, { observe: 'response' });
  }

  update(bookPageText: IBookPageText): Observable<EntityResponseType> {
    return this.http.put<IBookPageText>(`${this.resourceUrl}/${this.getBookPageTextIdentifier(bookPageText)}`, bookPageText, {
      observe: 'response',
    });
  }

  partialUpdate(bookPageText: PartialUpdateBookPageText): Observable<EntityResponseType> {
    return this.http.patch<IBookPageText>(`${this.resourceUrl}/${this.getBookPageTextIdentifier(bookPageText)}`, bookPageText, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IBookPageText>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBookPageText[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getBookPageTextIdentifier(bookPageText: Pick<IBookPageText, 'id'>): number {
    return bookPageText.id;
  }

  compareBookPageText(o1: Pick<IBookPageText, 'id'> | null, o2: Pick<IBookPageText, 'id'> | null): boolean {
    return o1 && o2 ? this.getBookPageTextIdentifier(o1) === this.getBookPageTextIdentifier(o2) : o1 === o2;
  }

  addBookPageTextToCollectionIfMissing<Type extends Pick<IBookPageText, 'id'>>(
    bookPageTextCollection: Type[],
    ...bookPageTextsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const bookPageTexts: Type[] = bookPageTextsToCheck.filter(isPresent);
    if (bookPageTexts.length > 0) {
      const bookPageTextCollectionIdentifiers = bookPageTextCollection.map(bookPageTextItem =>
        this.getBookPageTextIdentifier(bookPageTextItem),
      );
      const bookPageTextsToAdd = bookPageTexts.filter(bookPageTextItem => {
        const bookPageTextIdentifier = this.getBookPageTextIdentifier(bookPageTextItem);
        if (bookPageTextCollectionIdentifiers.includes(bookPageTextIdentifier)) {
          return false;
        }
        bookPageTextCollectionIdentifiers.push(bookPageTextIdentifier);
        return true;
      });
      return [...bookPageTextsToAdd, ...bookPageTextCollection];
    }
    return bookPageTextCollection;
  }
}
