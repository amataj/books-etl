import book from 'app/entities/book/book.reducer';
import bookFile from 'app/entities/book-file/book-file.reducer';
import bookPageText from 'app/entities/book-page-text/book-page-text.reducer';
import ingestRun from 'app/entities/ingest-run/ingest-run.reducer';
import ingestEvent from 'app/entities/ingest-event/ingest-event.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  book,
  bookFile,
  bookPageText,
  ingestRun,
  ingestEvent,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
