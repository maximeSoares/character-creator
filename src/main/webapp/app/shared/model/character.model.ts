import { IUser } from './user.model';
import { ISkill } from 'app/shared/model//skill.model';
import { IExperiencePoint } from 'app/shared/model//experience-point.model';

export interface ICharacter {
  id?: number;
  name?: string;
  user?: IUser;
  skills?: ISkill[];
  experiencePoints?: IExperiencePoint[];
}

export const defaultValue: Readonly<ICharacter> = {};
