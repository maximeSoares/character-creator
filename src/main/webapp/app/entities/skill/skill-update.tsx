import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { ICharacter } from 'app/shared/model/character.model';
import { getEntities as getCharacters } from 'app/entities/character/character.reducer';
import { ICharacterClass } from 'app/shared/model/character-class.model';
import { getEntities as getCharacterClasses } from 'app/entities/character-class/character-class.reducer';
import { ICharacterRace } from 'app/shared/model/character-race.model';
import { getEntities as getCharacterRaces } from 'app/entities/character-race/character-race.reducer';
import { getEntity, updateEntity, createEntity, reset } from './skill.reducer';
import { ISkill } from 'app/shared/model/skill.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer } from 'app/shared/util/date-utils';
import { keysToValues } from 'app/shared/util/entity-utils';

export interface ISkillUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: number }> {}

export interface ISkillUpdateState {
  isNew: boolean;
  characterId: number;
  characterClassId: number;
  characterRaceId: number;
}

export class SkillUpdate extends React.Component<ISkillUpdateProps, ISkillUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      characterId: 0,
      characterClassId: 0,
      characterRaceId: 0,
      isNew: !this.props.match.params || !this.props.match.params.id
    };
  }

  componentDidMount() {
    if (this.state.isNew) {
      this.props.reset();
    } else {
      this.props.getEntity(this.props.match.params.id);
    }

    this.props.getCharacters();
    this.props.getCharacterClasses();
    this.props.getCharacterRaces();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { skillEntity } = this.props;
      const entity = {
        ...skillEntity,
        ...values
      };

      if (this.state.isNew) {
        this.props.createEntity(entity);
      } else {
        this.props.updateEntity(entity);
      }
      this.handleClose();
    }
  };

  handleClose = () => {
    this.props.history.push('/entity/skill');
  };

  render() {
    const { skillEntity, characters, characterClasses, characterRaces, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="characterCreatorApp.skill.home.createOrEditLabel">
              <Translate contentKey="characterCreatorApp.skill.home.createOrEditLabel">Create or edit a Skill</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : skillEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="skill-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="titleLabel" for="title">
                    <Translate contentKey="characterCreatorApp.skill.title">Title</Translate>
                  </Label>
                  <AvField
                    id="skill-title"
                    type="text"
                    name="title"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="descriptionShortLabel" for="descriptionShort">
                    <Translate contentKey="characterCreatorApp.skill.descriptionShort">Description Short</Translate>
                  </Label>
                  <AvField id="skill-descriptionShort" type="text" name="descriptionShort" />
                </AvGroup>
                <AvGroup>
                  <Label id="descriptionLongLabel" for="descriptionLong">
                    <Translate contentKey="characterCreatorApp.skill.descriptionLong">Description Long</Translate>
                  </Label>
                  <AvField id="skill-descriptionLong" type="text" name="descriptionLong" />
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/skill" replace color="info">
                  <FontAwesomeIcon icon="arrow-left" />&nbsp;
                  <span className="d-none d-md-inline">
                    <Translate contentKey="entity.action.back">Back</Translate>
                  </span>
                </Button>
                &nbsp;
                <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                  <FontAwesomeIcon icon="save" />&nbsp;
                  <Translate contentKey="entity.action.save">Save</Translate>
                </Button>
              </AvForm>
            )}
          </Col>
        </Row>
      </div>
    );
  }
}

const mapStateToProps = (storeState: IRootState) => ({
  characters: storeState.character.entities,
  characterClasses: storeState.characterClass.entities,
  characterRaces: storeState.characterRace.entities,
  skillEntity: storeState.skill.entity,
  loading: storeState.skill.loading,
  updating: storeState.skill.updating
});

const mapDispatchToProps = {
  getCharacters,
  getCharacterClasses,
  getCharacterRaces,
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(SkillUpdate);
