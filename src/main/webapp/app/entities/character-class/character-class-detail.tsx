import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './character-class.reducer';
import { ICharacterClass } from 'app/shared/model/character-class.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ICharacterClassDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: number }> {}

export class CharacterClassDetail extends React.Component<ICharacterClassDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { characterClassEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="characterCreatorApp.characterClass.detail.title">CharacterClass</Translate> [<b>
              {characterClassEntity.id}
            </b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="name">
                <Translate contentKey="characterCreatorApp.characterClass.name">Name</Translate>
              </span>
            </dt>
            <dd>{characterClassEntity.name}</dd>
            <dt>
              <span id="description">
                <Translate contentKey="characterCreatorApp.characterClass.description">Description</Translate>
              </span>
            </dt>
            <dd>{characterClassEntity.description}</dd>
            <dt>
              <Translate contentKey="characterCreatorApp.characterClass.skill">Skill</Translate>
            </dt>
            <dd>
              {characterClassEntity.skills
                ? characterClassEntity.skills.map((val, i) => (
                    <span key={val.id}>
                      <a>{val.id}</a>
                      {i === characterClassEntity.skills.length - 1 ? '' : ', '}
                    </span>
                  ))
                : null}
            </dd>
          </dl>
          <Button tag={Link} to="/entity/character-class" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>&nbsp;
          <Button tag={Link} to={`/entity/character-class/${characterClassEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ characterClass }: IRootState) => ({
  characterClassEntity: characterClass.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(CharacterClassDetail);
