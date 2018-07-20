import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './skill.reducer';
import { ISkill } from 'app/shared/model/skill.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ISkillDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: number }> {}

export class SkillDetail extends React.Component<ISkillDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { skillEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="characterCreatorApp.skill.detail.title">Skill</Translate> [<b>{skillEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="title">
                <Translate contentKey="characterCreatorApp.skill.title">Title</Translate>
              </span>
            </dt>
            <dd>{skillEntity.title}</dd>
            <dt>
              <span id="descriptionShort">
                <Translate contentKey="characterCreatorApp.skill.descriptionShort">Description Short</Translate>
              </span>
            </dt>
            <dd>{skillEntity.descriptionShort}</dd>
            <dt>
              <span id="descriptionLong">
                <Translate contentKey="characterCreatorApp.skill.descriptionLong">Description Long</Translate>
              </span>
            </dt>
            <dd>{skillEntity.descriptionLong}</dd>
          </dl>
          <Button tag={Link} to="/entity/skill" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>&nbsp;
          <Button tag={Link} to={`/entity/skill/${skillEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ skill }: IRootState) => ({
  skillEntity: skill.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(SkillDetail);
