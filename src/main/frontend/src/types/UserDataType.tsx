type UserDataType = {
  id: number;
  name: string;
  email: string;
  role: string;
};

export const defaultUserDataType: UserDataType = {
  id: -1,
  name: 'Mr User',
  email: 'user@email.com',
  role: 'none',
};

export default UserDataType;
