import React from 'react';
import User from './UserType';

interface AppContextType extends React.Context<any> {
  user: User;
}

export default AppContextType;
