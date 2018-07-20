import { ISkill } from 'app/shared/model//skill.model';

export interface ICharacterClass {
  id?: number;
  name?: string;
  description?: string;
  skills?: ISkill[];
}

export const defaultValue: Readonly<ICharacterClass> = {};
