import React from 'react';
import { Switch } from 'react-router-dom';

// tslint:disable-next-line:no-unused-variable
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Character from './character';
import CharacterRace from './character-race';
import CharacterClass from './character-class';
import ExperiencePoint from './experience-point';
import Skill from './skill';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}/character`} component={Character} />
      <ErrorBoundaryRoute path={`${match.url}/character-race`} component={CharacterRace} />
      <ErrorBoundaryRoute path={`${match.url}/character-class`} component={CharacterClass} />
      <ErrorBoundaryRoute path={`${match.url}/experience-point`} component={ExperiencePoint} />
      <ErrorBoundaryRoute path={`${match.url}/skill`} component={Skill} />
      {/* jhipster-needle-add-route-path - JHipster will routes here */}
    </Switch>
  </div>
);

export default Routes;
