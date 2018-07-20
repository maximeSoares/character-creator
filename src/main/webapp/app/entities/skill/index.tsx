import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Skill from './skill';
import SkillDetail from './skill-detail';
import SkillUpdate from './skill-update';
import SkillDeleteDialog from './skill-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={SkillUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={SkillUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={SkillDetail} />
      <ErrorBoundaryRoute path={match.url} component={Skill} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={SkillDeleteDialog} />
  </>
);

export default Routes;
