import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBookFile } from '../book-file.model';
import { BookFileService } from '../service/book-file.service';

const bookFileResolve = (route: ActivatedRouteSnapshot): Observable<null | IBookFile> => {
  const id = route.params.id;
  if (id) {
    return inject(BookFileService)
      .find(id)
      .pipe(
        mergeMap((bookFile: HttpResponse<IBookFile>) => {
          if (bookFile.body) {
            return of(bookFile.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default bookFileResolve;
