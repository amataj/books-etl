import NavbarItem from 'app/layouts/navbar/navbar-item.model';

export const EntityNavbarItems: NavbarItem[] = [
  {
    name: 'Book',
    route: '/book',
    translationKey: 'global.menu.entities.book',
  },
  {
    name: 'BookFile',
    route: '/book-file',
    translationKey: 'global.menu.entities.bookFile',
  },
  {
    name: 'BookPageText',
    route: '/book-page-text',
    translationKey: 'global.menu.entities.bookPageText',
  },
  {
    name: 'IngestRun',
    route: '/ingest-run',
    translationKey: 'global.menu.entities.ingestRun',
  },
  {
    name: 'IngestEvent',
    route: '/ingest-event',
    translationKey: 'global.menu.entities.ingestEvent',
  },
];
