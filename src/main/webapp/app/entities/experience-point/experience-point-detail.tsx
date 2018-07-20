import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './experience-point.reducer';
import { IExperiencePoint } from 'app/shared/model/experience-point.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IExperiencePointDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: number }> {}

export class ExperiencePointDetail extends React.Component<IExperiencePointDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { experiencePointEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="characterCreatorApp.experiencePoint.detail.title">ExperiencePoint</Translate> [<b>
              {experiencePointEntity.id}
            </b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="acquisitionDate">
                <Translate contentKey="characterCreatorApp.experiencePoint.acquisitionDate">Acquisition Date</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={experiencePointEntity.acquisitionDate} type="date" format={APP_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="description">
                <Translate contentKey="characterCreatorApp.experiencePoint.description">Description</Translate>
              </span>
            </dt>
            <dd>{experiencePointEntity.description}</dd>
            <dt>
              <span id="startingExperiencePoint">
                <Translate contentKey="characterCreatorApp.experiencePoint.startingExperiencePoint">Starting Experience Point</Translate>
              </span>
            </dt>
            <dd>{experiencePointEntity.startingExperiencePoint ? 'true' : 'false'}</dd>
            <dt>
              <Translate contentKey="characterCreatorApp.experiencePoint.character">Character</Translate>
            </dt>
            <dd>{experiencePointEntity.character ? experiencePointEntity.character.id : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/experience-point" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>&nbsp;
          <Button tag={Link} to={`/entity/experience-point/${experiencePointEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.edit">Edit</Translate>
            </span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ experiencePoint }: IRootState) => ({
  experiencePointEntity: experiencePoint.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ExperiencePointDetail);
