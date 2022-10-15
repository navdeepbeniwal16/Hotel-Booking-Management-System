import RoleT, { Roles } from './RoleTypes';

type UserDataType = {
  id: number;
  name: string;
  email: string;
  role: RoleT;
  username: string;
  group: number;
};

export const defaultUserData: UserDataType = {
  id: -1,
  name: 'Mr User',
  email: 'user@email.com',
  role: Roles.NONE,
  username: 'user@email.com',
  group: -1,
};

export default UserDataType;
