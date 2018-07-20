import { Moment } from 'moment';
import { ICharacter } from 'app/shared/model//character.model';

export interface IExperiencePoint {
  id?: number;
  acquisitionDate?: Moment;
  description?: string;
  startingExperiencePoint?: boolean;
  character?: ICharacter;
}

export const defaultValue: Readonly<IExperiencePoint> = {
  startingExperiencePoint: false
};
