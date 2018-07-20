import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import CharacterRace from './character-race';
import CharacterRaceDetail from './character-race-detail';
import CharacterRaceUpdate from './character-race-update';
import CharacterRaceDeleteDialog from './character-race-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={CharacterRaceUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={CharacterRaceUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={CharacterRaceDetail} />
      <ErrorBoundaryRoute path={match.url} component={CharacterRace} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={CharacterRaceDeleteDialog} />
  </>
);

export default Routes;
