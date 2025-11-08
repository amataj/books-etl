import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IIngestEvent, NewIngestEvent } from '../ingest-event.model';

export type PartialUpdateIngestEvent = Partial<IIngestEvent> & Pick<IIngestEvent, 'id'>;

type RestOf<T extends IIngestEvent | NewIngestEvent> = Omit<T, 'createdAt'> & {
  createdAt?: string | null;
};

export type RestIngestEvent = RestOf<IIngestEvent>;

export type NewRestIngestEvent = RestOf<NewIngestEvent>;

export type PartialUpdateRestIngestEvent = RestOf<PartialUpdateIngestEvent>;

export type EntityResponseType = HttpResponse<IIngestEvent>;
export type EntityArrayResponseType = HttpResponse<IIngestEvent[]>;

@Injectable({ providedIn: 'root' })
export class IngestEventService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/ingest-events');

  create(ingestEvent: NewIngestEvent): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(ingestEvent);
    return this.http
      .post<RestIngestEvent>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(ingestEvent: IIngestEvent): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(ingestEvent);
    return this.http
      .put<RestIngestEvent>(`${this.resourceUrl}/${this.getIngestEventIdentifier(ingestEvent)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(ingestEvent: PartialUpdateIngestEvent): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(ingestEvent);
    return this.http
      .patch<RestIngestEvent>(`${this.resourceUrl}/${this.getIngestEventIdentifier(ingestEvent)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestIngestEvent>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestIngestEvent[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getIngestEventIdentifier(ingestEvent: Pick<IIngestEvent, 'id'>): number {
    return ingestEvent.id;
  }

  compareIngestEvent(o1: Pick<IIngestEvent, 'id'> | null, o2: Pick<IIngestEvent, 'id'> | null): boolean {
    return o1 && o2 ? this.getIngestEventIdentifier(o1) === this.getIngestEventIdentifier(o2) : o1 === o2;
  }

  addIngestEventToCollectionIfMissing<Type extends Pick<IIngestEvent, 'id'>>(
    ingestEventCollection: Type[],
    ...ingestEventsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const ingestEvents: Type[] = ingestEventsToCheck.filter(isPresent);
    if (ingestEvents.length > 0) {
      const ingestEventCollectionIdentifiers = ingestEventCollection.map(ingestEventItem => this.getIngestEventIdentifier(ingestEventItem));
      const ingestEventsToAdd = ingestEvents.filter(ingestEventItem => {
        const ingestEventIdentifier = this.getIngestEventIdentifier(ingestEventItem);
        if (ingestEventCollectionIdentifiers.includes(ingestEventIdentifier)) {
          return false;
        }
        ingestEventCollectionIdentifiers.push(ingestEventIdentifier);
        return true;
      });
      return [...ingestEventsToAdd, ...ingestEventCollection];
    }
    return ingestEventCollection;
  }

  protected convertDateFromClient<T extends IIngestEvent | NewIngestEvent | PartialUpdateIngestEvent>(ingestEvent: T): RestOf<T> {
    return {
      ...ingestEvent,
      createdAt: ingestEvent.createdAt?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restIngestEvent: RestIngestEvent): IIngestEvent {
    return {
      ...restIngestEvent,
      createdAt: restIngestEvent.createdAt ? dayjs(restIngestEvent.createdAt) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestIngestEvent>): HttpResponse<IIngestEvent> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestIngestEvent[]>): HttpResponse<IIngestEvent[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
