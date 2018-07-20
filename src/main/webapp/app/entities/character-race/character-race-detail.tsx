import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './character-race.reducer';
import { ICharacterRace } from 'app/shared/model/character-race.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ICharacterRaceDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: number }> {}

export class CharacterRaceDetail extends React.Component<ICharacterRaceDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { characterRaceEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="characterCreatorApp.characterRace.detail.title">CharacterRace</Translate> [<b>
              {characterRaceEntity.id}
            </b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="name">
                <Translate contentKey="characterCreatorApp.characterRace.name">Name</Translate>
              </span>
            </dt>
            <dd>{characterRaceEntity.name}</dd>
            <dt>
              <span id="description">
                <Translate contentKey="characterCreatorApp.characterRace.description">Description</Translate>
              </span>
            </dt>
            <dd>{characterRaceEntity.description}</dd>
            <dt>
              <Translate contentKey="characterCreatorApp.characterRace.skill">Skill</Translate>
            </dt>
            <dd>
              {characterRaceEntity.skills
                ? characterRaceEntity.skills.map((val, i) => (
                    <span key={val.id}>
                      <a>{val.id}</a>
                      {i === characterRaceEntity.skills.length - 1 ? '' : ', '}
                    </span>
                  ))
                : null}
            </dd>
          </dl>
          <Button tag={Link} to="/entity/character-race" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>&nbsp;
          <Button tag={Link} to={`/entity/character-race/${characterRaceEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ characterRace }: IRootState) => ({
  characterRaceEntity: characterRace.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(CharacterRaceDetail);
