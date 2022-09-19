import React from 'react';
import User from './UserType';
import { HotelState } from './HotelType';

interface AppContextType extends React.Context<any> {
  user: User;
  hotelState: HotelState;
}

export default AppContextType;
