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

const defaultRoles: RoleT[] = [];
const defaultToken = '';

const defaultUserMetadata: UserMetadata = {
  apiAccessToken: defaultToken,
  roles: defaultRoles,
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
  const { isAuthenticated, getAccessTokenSilently } = useAuth0();
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
      let apiAccessToken = '';
      try {
        apiAccessToken = await getAccessTokenSilently({
          audience: `${process.env.REACT_APP_AUTH0_AUDIENCE}`,
        });
      } catch (e: any) {
        if (e.error !== 'consent_required') {
          // console.log(e.message);
        }
      }

      if (isAuthenticated && process.env.REACT_APP_ENV === 'dev') {
        console.log('Development mode');
        console.log(apiAccessToken);
      }

      const decodedToken: JwtT = jwtDecode(apiAccessToken);
      let roles: RoleT[] = defaultRoles;
      if (decodedToken['lans_hotels/roles']) {
        roles = decodedToken['lans_hotels/roles'];
      }

      setUserMetadata((previousMetadata) => {
        if (!backend.compareAccessToken(apiAccessToken)) {
          setBackend(new LANS_API(apiAccessToken));
        }
        const newMetadata = {
          ...previousMetadata,
          roles,
          apiAccessToken,
        };
        return newMetadata;
      });
    };
    if (isAuthenticated) getUserMetadata();
    return () => {
      setUserMetadata(defaultUserMetadata);
    };
  }, [isAuthenticated]);

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
