import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/book">
        <Translate contentKey="global.menu.entities.book" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/book-file">
        <Translate contentKey="global.menu.entities.bookFile" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/book-page-text">
        <Translate contentKey="global.menu.entities.bookPageText" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/ingest-run">
        <Translate contentKey="global.menu.entities.ingestRun" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/ingest-event">
        <Translate contentKey="global.menu.entities.ingestEvent" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/books-etl-kafka">
        <Translate contentKey="global.menu.entities.booksEtlKafka" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
