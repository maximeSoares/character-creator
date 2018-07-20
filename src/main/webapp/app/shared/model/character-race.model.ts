import { ISkill } from 'app/shared/model//skill.model';

export interface ICharacterRace {
  id?: number;
  name?: string;
  description?: string;
  skills?: ISkill[];
}

export const defaultValue: Readonly<ICharacterRace> = {};
