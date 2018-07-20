import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Character from './character';
import CharacterDetail from './character-detail';
import CharacterUpdate from './character-update';
import CharacterDeleteDialog from './character-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={CharacterUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={CharacterUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={CharacterDetail} />
      <ErrorBoundaryRoute path={match.url} component={Character} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={CharacterDeleteDialog} />
  </>
);

export default Routes;
