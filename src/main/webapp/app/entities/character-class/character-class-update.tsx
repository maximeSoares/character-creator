import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { ISkill } from 'app/shared/model/skill.model';
import { getEntities as getSkills } from 'app/entities/skill/skill.reducer';
import { getEntity, updateEntity, createEntity, reset } from './character-class.reducer';
import { ICharacterClass } from 'app/shared/model/character-class.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer } from 'app/shared/util/date-utils';
import { keysToValues } from 'app/shared/util/entity-utils';

export interface ICharacterClassUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: number }> {}

export interface ICharacterClassUpdateState {
  isNew: boolean;
  idsskill: any[];
}

export class CharacterClassUpdate extends React.Component<ICharacterClassUpdateProps, ICharacterClassUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      idsskill: [],
      isNew: !this.props.match.params || !this.props.match.params.id
    };
  }

  componentDidMount() {
    if (this.state.isNew) {
      this.props.reset();
    } else {
      this.props.getEntity(this.props.match.params.id);
    }

    this.props.getSkills();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { characterClassEntity } = this.props;
      const entity = {
        ...characterClassEntity,
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
    this.props.history.push('/entity/character-class');
  };

  skillUpdate = element => {
    const selected = Array.from(element.target.selectedOptions).map((e: any) => parseInt(e.value, 10));
    this.setState({
      idsskill: keysToValues(selected, this.props.skills, 'id')
    });
  };

  displayskill(value: any) {
    if (this.state.idsskill && this.state.idsskill.length !== 0) {
      const list = [];
      for (const i in this.state.idsskill) {
        if (this.state.idsskill[i]) {
          list.push(this.state.idsskill[i].id);
        }
      }
      return list;
    }
    if (value.skills && value.skills.length !== 0) {
      const list = [];
      for (const i in value.skills) {
        if (value.skills[i]) {
          list.push(value.skills[i].id);
        }
      }
      this.setState({
        idsskill: keysToValues(list, this.props.skills, 'id')
      });
      return list;
    }
    return null;
  }

  render() {
    const { characterClassEntity, skills, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="characterCreatorApp.characterClass.home.createOrEditLabel">
              <Translate contentKey="characterCreatorApp.characterClass.home.createOrEditLabel">Create or edit a CharacterClass</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : characterClassEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="character-class-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="nameLabel" for="name">
                    <Translate contentKey="characterCreatorApp.characterClass.name">Name</Translate>
                  </Label>
                  <AvField
                    id="character-class-name"
                    type="text"
                    name="name"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="descriptionLabel" for="description">
                    <Translate contentKey="characterCreatorApp.characterClass.description">Description</Translate>
                  </Label>
                  <AvField
                    id="character-class-description"
                    type="text"
                    name="description"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label for="skills">
                    <Translate contentKey="characterCreatorApp.characterClass.skill">Skill</Translate>
                  </Label>
                  <AvInput
                    id="character-class-skill"
                    type="select"
                    multiple
                    className="form-control"
                    name="fakeskills"
                    value={this.displayskill(characterClassEntity)}
                    onChange={this.skillUpdate}
                  >
                    <option value="" key="0" />
                    {skills
                      ? skills.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                  <AvInput id="character-class-skill" type="hidden" name="skills" value={this.state.idsskill} />
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/character-class" replace color="info">
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
  skills: storeState.skill.entities,
  characterClassEntity: storeState.characterClass.entity,
  loading: storeState.characterClass.loading,
  updating: storeState.characterClass.updating
});

const mapDispatchToProps = {
  getSkills,
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
)(CharacterClassUpdate);
