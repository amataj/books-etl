import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IIngestEvent } from '../ingest-event.model';
import { IngestEventService } from '../service/ingest-event.service';

const ingestEventResolve = (route: ActivatedRouteSnapshot): Observable<null | IIngestEvent> => {
  const id = route.params.id;
  if (id) {
    return inject(IngestEventService)
      .find(id)
      .pipe(
        mergeMap((ingestEvent: HttpResponse<IIngestEvent>) => {
          if (ingestEvent.body) {
            return of(ingestEvent.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default ingestEventResolve;
