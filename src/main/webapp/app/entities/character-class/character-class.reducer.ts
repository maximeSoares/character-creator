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

import { ICharacterClass, defaultValue } from 'app/shared/model/character-class.model';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';

export const ACTION_TYPES = {
  FETCH_CHARACTERCLASS_LIST: 'characterClass/FETCH_CHARACTERCLASS_LIST',
  FETCH_CHARACTERCLASS: 'characterClass/FETCH_CHARACTERCLASS',
  CREATE_CHARACTERCLASS: 'characterClass/CREATE_CHARACTERCLASS',
  UPDATE_CHARACTERCLASS: 'characterClass/UPDATE_CHARACTERCLASS',
  DELETE_CHARACTERCLASS: 'characterClass/DELETE_CHARACTERCLASS',
  RESET: 'characterClass/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ICharacterClass>,
  entity: defaultValue,
  links: { next: 0 },
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type CharacterClassState = Readonly<typeof initialState>;

// Reducer

export default (state: CharacterClassState = initialState, action): CharacterClassState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_CHARACTERCLASS_LIST):
    case REQUEST(ACTION_TYPES.FETCH_CHARACTERCLASS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_CHARACTERCLASS):
    case REQUEST(ACTION_TYPES.UPDATE_CHARACTERCLASS):
    case REQUEST(ACTION_TYPES.DELETE_CHARACTERCLASS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_CHARACTERCLASS_LIST):
    case FAILURE(ACTION_TYPES.FETCH_CHARACTERCLASS):
    case FAILURE(ACTION_TYPES.CREATE_CHARACTERCLASS):
    case FAILURE(ACTION_TYPES.UPDATE_CHARACTERCLASS):
    case FAILURE(ACTION_TYPES.DELETE_CHARACTERCLASS):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_CHARACTERCLASS_LIST):
      const links = parseHeaderForLinks(action.payload.headers.link);
      return {
        ...state,
        links,
        loading: false,
        totalItems: action.payload.headers['x-total-count'],
        entities: loadMoreDataWhenScrolled(state.entities, action.payload.data, links, ITEMS_PER_PAGE)
      };
    case SUCCESS(ACTION_TYPES.FETCH_CHARACTERCLASS):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_CHARACTERCLASS):
    case SUCCESS(ACTION_TYPES.UPDATE_CHARACTERCLASS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_CHARACTERCLASS):
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

const apiUrl = 'api/character-classes';

// Actions

export const getEntities: ICrudGetAllAction<ICharacterClass> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_CHARACTERCLASS_LIST,
    payload: axios.get<ICharacterClass>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<ICharacterClass> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_CHARACTERCLASS,
    payload: axios.get<ICharacterClass>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<ICharacterClass> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_CHARACTERCLASS,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ICharacterClass> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_CHARACTERCLASS,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<ICharacterClass> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_CHARACTERCLASS,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
