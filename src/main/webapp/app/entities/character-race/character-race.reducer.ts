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

import { ICharacterRace, defaultValue } from 'app/shared/model/character-race.model';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';

export const ACTION_TYPES = {
  FETCH_CHARACTERRACE_LIST: 'characterRace/FETCH_CHARACTERRACE_LIST',
  FETCH_CHARACTERRACE: 'characterRace/FETCH_CHARACTERRACE',
  CREATE_CHARACTERRACE: 'characterRace/CREATE_CHARACTERRACE',
  UPDATE_CHARACTERRACE: 'characterRace/UPDATE_CHARACTERRACE',
  DELETE_CHARACTERRACE: 'characterRace/DELETE_CHARACTERRACE',
  RESET: 'characterRace/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ICharacterRace>,
  entity: defaultValue,
  links: { next: 0 },
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type CharacterRaceState = Readonly<typeof initialState>;

// Reducer

export default (state: CharacterRaceState = initialState, action): CharacterRaceState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_CHARACTERRACE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_CHARACTERRACE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_CHARACTERRACE):
    case REQUEST(ACTION_TYPES.UPDATE_CHARACTERRACE):
    case REQUEST(ACTION_TYPES.DELETE_CHARACTERRACE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_CHARACTERRACE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_CHARACTERRACE):
    case FAILURE(ACTION_TYPES.CREATE_CHARACTERRACE):
    case FAILURE(ACTION_TYPES.UPDATE_CHARACTERRACE):
    case FAILURE(ACTION_TYPES.DELETE_CHARACTERRACE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_CHARACTERRACE_LIST):
      const links = parseHeaderForLinks(action.payload.headers.link);
      return {
        ...state,
        links,
        loading: false,
        totalItems: action.payload.headers['x-total-count'],
        entities: loadMoreDataWhenScrolled(state.entities, action.payload.data, links, ITEMS_PER_PAGE)
      };
    case SUCCESS(ACTION_TYPES.FETCH_CHARACTERRACE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_CHARACTERRACE):
    case SUCCESS(ACTION_TYPES.UPDATE_CHARACTERRACE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_CHARACTERRACE):
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

const apiUrl = 'api/character-races';

// Actions

export const getEntities: ICrudGetAllAction<ICharacterRace> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_CHARACTERRACE_LIST,
    payload: axios.get<ICharacterRace>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<ICharacterRace> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_CHARACTERRACE,
    payload: axios.get<ICharacterRace>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<ICharacterRace> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_CHARACTERRACE,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ICharacterRace> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_CHARACTERRACE,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<ICharacterRace> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_CHARACTERRACE,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
