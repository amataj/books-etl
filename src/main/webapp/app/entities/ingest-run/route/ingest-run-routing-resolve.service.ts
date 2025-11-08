import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IIngestRun } from '../ingest-run.model';
import { IngestRunService } from '../service/ingest-run.service';

const ingestRunResolve = (route: ActivatedRouteSnapshot): Observable<null | IIngestRun> => {
  const id = route.params.id;
  if (id) {
    return inject(IngestRunService)
      .find(id)
      .pipe(
        mergeMap((ingestRun: HttpResponse<IIngestRun>) => {
          if (ingestRun.body) {
            return of(ingestRun.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default ingestRunResolve;
