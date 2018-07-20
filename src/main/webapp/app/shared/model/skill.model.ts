import { ICharacter } from 'app/shared/model//character.model';
import { ICharacterClass } from 'app/shared/model//character-class.model';
import { ICharacterRace } from 'app/shared/model//character-race.model';

export interface ISkill {
  id?: number;
  title?: string;
  descriptionShort?: string;
  descriptionLong?: string;
  characters?: ICharacter[];
  characterClasses?: ICharacterClass[];
  characterRaces?: ICharacterRace[];
}

export const defaultValue: Readonly<ISkill> = {};
