import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBookFile, NewBookFile } from '../book-file.model';

export type PartialUpdateBookFile = Partial<IBookFile> & Pick<IBookFile, 'id'>;

type RestOf<T extends IBookFile | NewBookFile> = Omit<T, 'mtime' | 'firstSeenAt' | 'lastSeenAt'> & {
  mtime?: string | null;
  firstSeenAt?: string | null;
  lastSeenAt?: string | null;
};

export type RestBookFile = RestOf<IBookFile>;

export type NewRestBookFile = RestOf<NewBookFile>;

export type PartialUpdateRestBookFile = RestOf<PartialUpdateBookFile>;

export type EntityResponseType = HttpResponse<IBookFile>;
export type EntityArrayResponseType = HttpResponse<IBookFile[]>;

@Injectable({ providedIn: 'root' })
export class BookFileService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/book-files');

  create(bookFile: NewBookFile): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(bookFile);
    return this.http
      .post<RestBookFile>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(bookFile: IBookFile): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(bookFile);
    return this.http
      .put<RestBookFile>(`${this.resourceUrl}/${this.getBookFileIdentifier(bookFile)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(bookFile: PartialUpdateBookFile): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(bookFile);
    return this.http
      .patch<RestBookFile>(`${this.resourceUrl}/${this.getBookFileIdentifier(bookFile)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestBookFile>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestBookFile[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getBookFileIdentifier(bookFile: Pick<IBookFile, 'id'>): number {
    return bookFile.id;
  }

  compareBookFile(o1: Pick<IBookFile, 'id'> | null, o2: Pick<IBookFile, 'id'> | null): boolean {
    return o1 && o2 ? this.getBookFileIdentifier(o1) === this.getBookFileIdentifier(o2) : o1 === o2;
  }

  addBookFileToCollectionIfMissing<Type extends Pick<IBookFile, 'id'>>(
    bookFileCollection: Type[],
    ...bookFilesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const bookFiles: Type[] = bookFilesToCheck.filter(isPresent);
    if (bookFiles.length > 0) {
      const bookFileCollectionIdentifiers = bookFileCollection.map(bookFileItem => this.getBookFileIdentifier(bookFileItem));
      const bookFilesToAdd = bookFiles.filter(bookFileItem => {
        const bookFileIdentifier = this.getBookFileIdentifier(bookFileItem);
        if (bookFileCollectionIdentifiers.includes(bookFileIdentifier)) {
          return false;
        }
        bookFileCollectionIdentifiers.push(bookFileIdentifier);
        return true;
      });
      return [...bookFilesToAdd, ...bookFileCollection];
    }
    return bookFileCollection;
  }

  protected convertDateFromClient<T extends IBookFile | NewBookFile | PartialUpdateBookFile>(bookFile: T): RestOf<T> {
    return {
      ...bookFile,
      mtime: bookFile.mtime?.toJSON() ?? null,
      firstSeenAt: bookFile.firstSeenAt?.toJSON() ?? null,
      lastSeenAt: bookFile.lastSeenAt?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restBookFile: RestBookFile): IBookFile {
    return {
      ...restBookFile,
      mtime: restBookFile.mtime ? dayjs(restBookFile.mtime) : undefined,
      firstSeenAt: restBookFile.firstSeenAt ? dayjs(restBookFile.firstSeenAt) : undefined,
      lastSeenAt: restBookFile.lastSeenAt ? dayjs(restBookFile.lastSeenAt) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestBookFile>): HttpResponse<IBookFile> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestBookFile[]>): HttpResponse<IBookFile[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
