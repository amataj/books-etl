import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBookPageText } from '../book-page-text.model';
import { BookPageTextService } from '../service/book-page-text.service';

const bookPageTextResolve = (route: ActivatedRouteSnapshot): Observable<null | IBookPageText> => {
  const id = route.params.id;
  if (id) {
    return inject(BookPageTextService)
      .find(id)
      .pipe(
        mergeMap((bookPageText: HttpResponse<IBookPageText>) => {
          if (bookPageText.body) {
            return of(bookPageText.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default bookPageTextResolve;
