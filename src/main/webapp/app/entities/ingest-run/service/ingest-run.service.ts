import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IIngestRun, NewIngestRun } from '../ingest-run.model';

export type PartialUpdateIngestRun = Partial<IIngestRun> & Pick<IIngestRun, 'id'>;

type RestOf<T extends IIngestRun | NewIngestRun> = Omit<T, 'startedAt' | 'finishedAt'> & {
  startedAt?: string | null;
  finishedAt?: string | null;
};

export type RestIngestRun = RestOf<IIngestRun>;

export type NewRestIngestRun = RestOf<NewIngestRun>;

export type PartialUpdateRestIngestRun = RestOf<PartialUpdateIngestRun>;

export type EntityResponseType = HttpResponse<IIngestRun>;
export type EntityArrayResponseType = HttpResponse<IIngestRun[]>;

@Injectable({ providedIn: 'root' })
export class IngestRunService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/ingest-runs');

  create(ingestRun: NewIngestRun): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(ingestRun);
    return this.http
      .post<RestIngestRun>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(ingestRun: IIngestRun): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(ingestRun);
    return this.http
      .put<RestIngestRun>(`${this.resourceUrl}/${this.getIngestRunIdentifier(ingestRun)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(ingestRun: PartialUpdateIngestRun): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(ingestRun);
    return this.http
      .patch<RestIngestRun>(`${this.resourceUrl}/${this.getIngestRunIdentifier(ingestRun)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestIngestRun>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestIngestRun[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getIngestRunIdentifier(ingestRun: Pick<IIngestRun, 'id'>): number {
    return ingestRun.id;
  }

  compareIngestRun(o1: Pick<IIngestRun, 'id'> | null, o2: Pick<IIngestRun, 'id'> | null): boolean {
    return o1 && o2 ? this.getIngestRunIdentifier(o1) === this.getIngestRunIdentifier(o2) : o1 === o2;
  }

  addIngestRunToCollectionIfMissing<Type extends Pick<IIngestRun, 'id'>>(
    ingestRunCollection: Type[],
    ...ingestRunsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const ingestRuns: Type[] = ingestRunsToCheck.filter(isPresent);
    if (ingestRuns.length > 0) {
      const ingestRunCollectionIdentifiers = ingestRunCollection.map(ingestRunItem => this.getIngestRunIdentifier(ingestRunItem));
      const ingestRunsToAdd = ingestRuns.filter(ingestRunItem => {
        const ingestRunIdentifier = this.getIngestRunIdentifier(ingestRunItem);
        if (ingestRunCollectionIdentifiers.includes(ingestRunIdentifier)) {
          return false;
        }
        ingestRunCollectionIdentifiers.push(ingestRunIdentifier);
        return true;
      });
      return [...ingestRunsToAdd, ...ingestRunCollection];
    }
    return ingestRunCollection;
  }

  protected convertDateFromClient<T extends IIngestRun | NewIngestRun | PartialUpdateIngestRun>(ingestRun: T): RestOf<T> {
    return {
      ...ingestRun,
      startedAt: ingestRun.startedAt?.toJSON() ?? null,
      finishedAt: ingestRun.finishedAt?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restIngestRun: RestIngestRun): IIngestRun {
    return {
      ...restIngestRun,
      startedAt: restIngestRun.startedAt ? dayjs(restIngestRun.startedAt) : undefined,
      finishedAt: restIngestRun.finishedAt ? dayjs(restIngestRun.finishedAt) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestIngestRun>): HttpResponse<IIngestRun> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestIngestRun[]>): HttpResponse<IIngestRun[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
