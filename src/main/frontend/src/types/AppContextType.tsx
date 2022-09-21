import React from 'react';
import UserState from './UserType';
import { HotelState } from './HotelType';
import { RoomState } from './RoomType';

interface AppContextType extends React.Context<any> {
  user: UserState;
  hotelState: HotelState;
  roomState: RoomState;
}

export default AppContextType;
