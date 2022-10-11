import RoleT, { Roles } from './RoleTypes';

type UserDataType = {
  id: number;
  name: string;
  email: string;
  role: RoleT;
};

export const defaultUserDataType: UserDataType = {
  id: -1,
  name: 'Mr User',
  email: 'user@email.com',
  role: Roles.NONE,
};

export default UserDataType;
