import isEqual from 'lodash/isEqual';
import axios from 'axios';
import {
  parseHeaderForLinks,
  loadMoreDataWhenScrolled,
  ICrudGetAction,
  ICrudGetAllAction,
  ICrudPutAction,
  ICrudDeleteAction
} from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IExperiencePoint, defaultValue } from 'app/shared/model/experience-point.model';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';

export const ACTION_TYPES = {
  FETCH_EXPERIENCEPOINT_LIST: 'experiencePoint/FETCH_EXPERIENCEPOINT_LIST',
  FETCH_EXPERIENCEPOINT: 'experiencePoint/FETCH_EXPERIENCEPOINT',
  CREATE_EXPERIENCEPOINT: 'experiencePoint/CREATE_EXPERIENCEPOINT',
  UPDATE_EXPERIENCEPOINT: 'experiencePoint/UPDATE_EXPERIENCEPOINT',
  DELETE_EXPERIENCEPOINT: 'experiencePoint/DELETE_EXPERIENCEPOINT',
  RESET: 'experiencePoint/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IExperiencePoint>,
  entity: defaultValue,
  links: { next: 0 },
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type ExperiencePointState = Readonly<typeof initialState>;

// Reducer

export default (state: ExperiencePointState = initialState, action): ExperiencePointState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_EXPERIENCEPOINT_LIST):
    case REQUEST(ACTION_TYPES.FETCH_EXPERIENCEPOINT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_EXPERIENCEPOINT):
    case REQUEST(ACTION_TYPES.UPDATE_EXPERIENCEPOINT):
    case REQUEST(ACTION_TYPES.DELETE_EXPERIENCEPOINT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_EXPERIENCEPOINT_LIST):
    case FAILURE(ACTION_TYPES.FETCH_EXPERIENCEPOINT):
    case FAILURE(ACTION_TYPES.CREATE_EXPERIENCEPOINT):
    case FAILURE(ACTION_TYPES.UPDATE_EXPERIENCEPOINT):
    case FAILURE(ACTION_TYPES.DELETE_EXPERIENCEPOINT):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_EXPERIENCEPOINT_LIST):
      const links = parseHeaderForLinks(action.payload.headers.link);
      return {
        ...state,
        links,
        loading: false,
        totalItems: action.payload.headers['x-total-count'],
        entities: loadMoreDataWhenScrolled(state.entities, action.payload.data, links, ITEMS_PER_PAGE)
      };
    case SUCCESS(ACTION_TYPES.FETCH_EXPERIENCEPOINT):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_EXPERIENCEPOINT):
    case SUCCESS(ACTION_TYPES.UPDATE_EXPERIENCEPOINT):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_EXPERIENCEPOINT):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'api/experience-points';

// Actions

export const getEntities: ICrudGetAllAction<IExperiencePoint> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_EXPERIENCEPOINT_LIST,
    payload: axios.get<IExperiencePoint>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IExperiencePoint> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_EXPERIENCEPOINT,
    payload: axios.get<IExperiencePoint>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IExperiencePoint> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_EXPERIENCEPOINT,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IExperiencePoint> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_EXPERIENCEPOINT,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IExperiencePoint> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_EXPERIENCEPOINT,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
