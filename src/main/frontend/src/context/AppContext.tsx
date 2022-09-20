import React, {
  Context,
  createContext,
  useState,
  ReactNode,
  ReactPropTypes,
} from 'react';

import User from '../types/UserType';
import { defaultHotel, defaultHotelState } from '../types/HotelType';
import { defaultRoom, defaultRoomState } from '../types/RoomType';

const defaultUser: User = {
  username: '',
  setUsername: () =>
    console.error('Error: cannot call setUsername() without context'),
};

const defaultGlobalContext = {
  user: defaultUser,
  hotel: defaultHotelState,
  room: defaultRoomState,
};

const GlobalContext = createContext(defaultGlobalContext);

interface IGlobalProvider {
  children: ReactNode;
}

const GlobalProvider = ({ children }: IGlobalProvider) => {
  const [username, setUsername] = useState('');
  const [hotel, setHotel] = useState(defaultHotel);
  const [room, setRoom] = useState(defaultRoom);

  const userState = {
    username,
    setUsername,
  };

  const hotelState = {
    hotel,
    setHotel,
  };

  const roomState = {
    room,
    setRoom,
  };

  return (
    <GlobalContext.Provider
      value={{
        user: userState,
        hotel: hotelState,
        room: roomState,
      }}
    >
      {children}
    </GlobalContext.Provider>
  );
};

const withGlobalContext =
  (Child: React.FC<ReactPropTypes>) =>
  (props: ReactPropTypes, context: Context<any>) => {
    if (!Child) return null;
    return (
      <GlobalContext.Consumer>
        {(context) => <Child {...props} {...context} />}
      </GlobalContext.Consumer>
    );
  };

const AppContext = {
  GlobalContext,
  GlobalProvider,
  withGlobalContext,
};

export default AppContext;
