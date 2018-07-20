import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { ISkill } from 'app/shared/model/skill.model';
import { getEntities as getSkills } from 'app/entities/skill/skill.reducer';
import { getEntity, updateEntity, createEntity, reset } from './character.reducer';
import { ICharacter } from 'app/shared/model/character.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer } from 'app/shared/util/date-utils';
import { keysToValues } from 'app/shared/util/entity-utils';

export interface ICharacterUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: number }> {}

export interface ICharacterUpdateState {
  isNew: boolean;
  idsskill: any[];
  userId: number;
}

export class CharacterUpdate extends React.Component<ICharacterUpdateProps, ICharacterUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      idsskill: [],
      userId: 0,
      isNew: !this.props.match.params || !this.props.match.params.id
    };
  }

  componentDidMount() {
    if (this.state.isNew) {
      this.props.reset();
    } else {
      this.props.getEntity(this.props.match.params.id);
    }

    this.props.getUsers();
    this.props.getSkills();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { characterEntity } = this.props;
      const entity = {
        ...characterEntity,
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
    this.props.history.push('/entity/character');
  };

  userUpdate = element => {
    const id = element.target.value.toString();
    if (id === '') {
      this.setState({
        userId: -1
      });
    } else {
      for (const i in this.props.users) {
        if (id === this.props.users[i].id.toString()) {
          this.setState({
            userId: this.props.users[i].id
          });
        }
      }
    }
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
    const { characterEntity, users, skills, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="characterCreatorApp.character.home.createOrEditLabel">
              <Translate contentKey="characterCreatorApp.character.home.createOrEditLabel">Create or edit a Character</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : characterEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="character-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="nameLabel" for="name">
                    <Translate contentKey="characterCreatorApp.character.name">Name</Translate>
                  </Label>
                  <AvField
                    id="character-name"
                    type="text"
                    name="name"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label for="user.id">
                    <Translate contentKey="characterCreatorApp.character.user">User</Translate>
                  </Label>
                  <AvInput id="character-user" type="select" className="form-control" name="user.id" onChange={this.userUpdate}>
                    <option value="" key="0" />
                    {users
                      ? users.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="skills">
                    <Translate contentKey="characterCreatorApp.character.skill">Skill</Translate>
                  </Label>
                  <AvInput
                    id="character-skill"
                    type="select"
                    multiple
                    className="form-control"
                    name="fakeskills"
                    value={this.displayskill(characterEntity)}
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
                  <AvInput id="character-skill" type="hidden" name="skills" value={this.state.idsskill} />
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/character" replace color="info">
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
  users: storeState.userManagement.users,
  skills: storeState.skill.entities,
  characterEntity: storeState.character.entity,
  loading: storeState.character.loading,
  updating: storeState.character.updating
});

const mapDispatchToProps = {
  getUsers,
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
)(CharacterUpdate);
