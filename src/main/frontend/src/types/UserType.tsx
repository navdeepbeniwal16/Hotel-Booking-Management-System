import React, { Dispatch } from 'react';
import { EmptyString } from './PrimitiveTypes';

type UserState = {
  username: string | EmptyString;
  setUsername: Dispatch<React.SetStateAction<string>>;
};

export default UserState;
