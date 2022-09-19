import React, {
  Context,
  createContext,
  useState,
  ReactNode,
  ReactPropTypes,
} from 'react';

import User from '../types/UserType';
import Hotel, { HotelState } from '../types/HotelType';

const defaultUser: User = {
  username: '',
  setUsername: () =>
    console.error('Error: cannot call setUsername() without context'),
};

const defaultHotel: Hotel = {
  address: '',
  phone: '',
  name: '',
  id: -1,
  email: '',
};

const defaultHotelState: HotelState = {
  hotel: defaultHotel,
  setHotel: () =>
    console.error('Error: cannot call setHotel() without context'),
};

const defaultGlobalContext = {
  user: defaultUser,
  hotel: defaultHotelState,
};

const GlobalContext = createContext(defaultGlobalContext);

interface IGlobalProvider {
  children: ReactNode;
}

const GlobalProvider = ({ children }: IGlobalProvider) => {
  const [username, setUsername] = useState('');
  const [hotel, setHotel] = useState(defaultHotel);

  const userState = {
    username,
    setUsername,
  };

  const hotelState = {
    hotel,
    setHotel,
  };

  return (
    <GlobalContext.Provider value={{ user: userState, hotel: hotelState }}>
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
