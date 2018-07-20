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
import { getEntity, updateEntity, createEntity, reset } from './experience-point.reducer';
import { IExperiencePoint } from 'app/shared/model/experience-point.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer } from 'app/shared/util/date-utils';
import { keysToValues } from 'app/shared/util/entity-utils';

export interface IExperiencePointUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: number }> {}

export interface IExperiencePointUpdateState {
  isNew: boolean;
  characterId: number;
}

export class ExperiencePointUpdate extends React.Component<IExperiencePointUpdateProps, IExperiencePointUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      characterId: 0,
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
  }

  saveEntity = (event, errors, values) => {
    values.acquisitionDate = new Date(values.acquisitionDate);

    if (errors.length === 0) {
      const { experiencePointEntity } = this.props;
      const entity = {
        ...experiencePointEntity,
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
    this.props.history.push('/entity/experience-point');
  };

  characterUpdate = element => {
    const id = element.target.value.toString();
    if (id === '') {
      this.setState({
        characterId: -1
      });
    } else {
      for (const i in this.props.characters) {
        if (id === this.props.characters[i].id.toString()) {
          this.setState({
            characterId: this.props.characters[i].id
          });
        }
      }
    }
  };

  render() {
    const { experiencePointEntity, characters, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="characterCreatorApp.experiencePoint.home.createOrEditLabel">
              <Translate contentKey="characterCreatorApp.experiencePoint.home.createOrEditLabel">
                Create or edit a ExperiencePoint
              </Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : experiencePointEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="experience-point-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="acquisitionDateLabel" for="acquisitionDate">
                    <Translate contentKey="characterCreatorApp.experiencePoint.acquisitionDate">Acquisition Date</Translate>
                  </Label>
                  <AvInput
                    id="experience-point-acquisitionDate"
                    type="datetime-local"
                    className="form-control"
                    name="acquisitionDate"
                    value={isNew ? null : convertDateTimeFromServer(this.props.experiencePointEntity.acquisitionDate)}
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="descriptionLabel" for="description">
                    <Translate contentKey="characterCreatorApp.experiencePoint.description">Description</Translate>
                  </Label>
                  <AvField id="experience-point-description" type="text" name="description" />
                </AvGroup>
                <AvGroup>
                  <Label id="startingExperiencePointLabel" check>
                    <AvInput
                      id="experience-point-startingExperiencePoint"
                      type="checkbox"
                      className="form-control"
                      name="startingExperiencePoint"
                    />
                    <Translate contentKey="characterCreatorApp.experiencePoint.startingExperiencePoint">
                      Starting Experience Point
                    </Translate>
                  </Label>
                </AvGroup>
                <AvGroup>
                  <Label for="character.id">
                    <Translate contentKey="characterCreatorApp.experiencePoint.character">Character</Translate>
                  </Label>
                  <AvInput
                    id="experience-point-character"
                    type="select"
                    className="form-control"
                    name="character.id"
                    onChange={this.characterUpdate}
                  >
                    <option value="" key="0" />
                    {characters
                      ? characters.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/experience-point" replace color="info">
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
  experiencePointEntity: storeState.experiencePoint.entity,
  loading: storeState.experiencePoint.loading,
  updating: storeState.experiencePoint.updating
});

const mapDispatchToProps = {
  getCharacters,
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
)(ExperiencePointUpdate);
