import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ExperiencePoint from './experience-point';
import ExperiencePointDetail from './experience-point-detail';
import ExperiencePointUpdate from './experience-point-update';
import ExperiencePointDeleteDialog from './experience-point-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ExperiencePointUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ExperiencePointUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ExperiencePointDetail} />
      <ErrorBoundaryRoute path={match.url} component={ExperiencePoint} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={ExperiencePointDeleteDialog} />
  </>
);

export default Routes;
