import React, { Dispatch } from 'react';
import UserDataType, { defaultUserData } from './UserDataType';

type UserState = {
  user: UserDataType;
  setUser: Dispatch<React.SetStateAction<UserDataType>>;
};

export const defaultUserState: UserState = {
  user: defaultUserData,
  setUser: () =>
    console.error('Error: cannot call setUsername() without context'),
};

export default UserState;
