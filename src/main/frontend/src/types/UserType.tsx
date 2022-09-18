import React, { Dispatch } from 'react';
import { EmptyString } from './PrimitiveTypes';

type User = {
  username: string | EmptyString;
  setUsername: Dispatch<React.SetStateAction<string>>;
};

export default User;
