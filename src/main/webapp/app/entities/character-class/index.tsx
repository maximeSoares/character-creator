import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import CharacterClass from './character-class';
import CharacterClassDetail from './character-class-detail';
import CharacterClassUpdate from './character-class-update';
import CharacterClassDeleteDialog from './character-class-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={CharacterClassUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={CharacterClassUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={CharacterClassDetail} />
      <ErrorBoundaryRoute path={match.url} component={CharacterClass} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={CharacterClassDeleteDialog} />
  </>
);

export default Routes;
