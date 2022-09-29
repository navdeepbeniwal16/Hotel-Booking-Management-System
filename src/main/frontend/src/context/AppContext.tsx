/* eslint-disable @typescript-eslint/no-explicit-any */
import React, {
  Context,
  createContext,
  useState,
  ReactNode,
  useEffect,
  ReactPropTypes,
} from 'react';
import { useAuth0 } from '@auth0/auth0-react';
import UserState from '../types/UserType';
import { defaultHotel, defaultHotelState } from '../types/HotelType';
import { defaultRoom, defaultRoomState } from '../types/RoomType';
import AppContextType from '../types/AppContextType';
import RoleT from '../types/RoleTypes';
import JwtT from '../types/JwtType';
import jwtDecode from 'jwt-decode';
import LANS_API from '../api/API';

const defaultUser: UserState = {
  username: '',
  setUsername: () =>
    console.error('Error: cannot call setUsername() without context'),
};

export type UserMetadata = {
  apiAccessToken: string;
  roles: RoleT[];
};

const defaultUserMetadata: UserMetadata = {
  apiAccessToken: '',
  roles: [],
};

const defaultGlobalContext = {
  backend: new LANS_API(''),
  user: defaultUser,
  hotel: defaultHotelState,
  room: defaultRoomState,
  userMetadata: defaultUserMetadata,
};

const GlobalContext = createContext(defaultGlobalContext);

interface IGlobalProvider {
  children: ReactNode;
}

const GlobalProvider = ({ children }: IGlobalProvider) => {
  const { isAuthenticated, getAccessTokenSilently, getAccessTokenWithPopup } =
    useAuth0();
  const [username, setUsername] = useState('');
  const [hotel, setHotel] = useState(defaultHotel);
  const [room, setRoom] = useState(defaultRoom);
  const [userMetadata, setUserMetadata] = useState(defaultUserMetadata);
  const [backend, setBackend] = useState(new LANS_API(''));

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

  useEffect(() => {
    const getUserMetadata = async () => {
      let apiAccessToken: string;
      let lans_api: LANS_API;
      try {
        apiAccessToken = await getAccessTokenSilently({
          audience: `${process.env.REACT_APP_AUTH0_AUDIENCE}`,
        });
        lans_api = new LANS_API(apiAccessToken);
      } catch (e: any) {
        if (e.error === 'consent_required') {
          apiAccessToken = await getAccessTokenWithPopup({
            audience: `${process.env.REACT_APP_AUTH0_AUDIENCE}`,
          });
          lans_api = new LANS_API(apiAccessToken);
        } else {
          console.log(e.message);
          throw e;
        }
      }

      if (process.env.REACT_APP_ENV === 'dev') {
        console.log('Development mode');
        console.log('JWT:\n', apiAccessToken);
      }

      const decodedToken: JwtT = jwtDecode(apiAccessToken);

      setBackend(lans_api);
      setUserMetadata((previousMetadata) => {
        const newMetadata = {
          ...previousMetadata,
          roles: decodedToken['lans_hotels/roles'] || [],
          apiAccessToken,
        };
        return newMetadata;
      });
    };

    getUserMetadata();
    return () => {
      setUserMetadata(defaultUserMetadata);
    };
  }, [isAuthenticated, getAccessTokenSilently, getAccessTokenWithPopup]);

  return (
    <GlobalContext.Provider
      value={{
        backend,
        user: userState,
        hotel: hotelState,
        room: roomState,
        userMetadata,
      }}
    >
      {children}
    </GlobalContext.Provider>
  );
};

const withGlobalContext =
  (Child: React.FC<ReactPropTypes>) =>
  // eslint-disable-next-line react/display-name, @typescript-eslint/no-unused-vars
  (props: ReactPropTypes, _context: Context<AppContextType>) => {
    if (!Child) return null;
    return (
      <GlobalContext.Consumer>
        {(context: any) => <Child {...props} {...context} />}
      </GlobalContext.Consumer>
    );
  };

const AppContext = {
  GlobalContext,
  GlobalProvider,
  withGlobalContext,
};

export default AppContext;
