type UserDataType = {
  id: number;
  name: string;
  email: string;
  role: number;
};

export const defaultUserDataType: UserDataType = {
  id: -1,
  name: '',
  email: '',
  role: -1,
};

export default UserDataType;
