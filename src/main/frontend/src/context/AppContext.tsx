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
import UserState, { defaultUserState } from '../types/UserType';
import { defaultUserData } from '../types/UserDataType';
import { defaultHotel, defaultHotelState } from '../types/HotelType';
import { defaultRoom, defaultRoomState } from '../types/RoomType';
import AppContextType from '../types/AppContextType';
import RoleT from '../types/RoleTypes';
import JwtT from '../types/JwtType';
import jwtDecode from 'jwt-decode';
import LANS_API from '../api/API';

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
  user: defaultUserState,
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
  const [user, setUser] = useState(defaultUserData);
  const [hotel, setHotel] = useState(defaultHotel);
  const [room, setRoom] = useState(defaultRoom);
  const [userMetadata, setUserMetadata] = useState(defaultUserMetadata);
  const [backend, setBackend] = useState(new LANS_API(''));

  const userState: UserState = {
    user,
    setUser,
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
        if (isAuthenticated && process.env.REACT_APP_SHOW_JWT === "true") {
          console.log('Development mode');
          console.log(apiAccessToken);
        }
        let roles: RoleT[] = defaultRoles;
        if (apiAccessToken !== '') {
          const decodedToken: JwtT = jwtDecode(apiAccessToken);
          if (decodedToken['lans_hotels/roles']) {
            roles = decodedToken['lans_hotels/roles'];
          }
        }

        if (isAuthenticated) {
          console.log('authenticated');
          const newBackendConnection = new LANS_API(apiAccessToken);
          const [success, userData] = await newBackendConnection.register();
          if (success) {
            setUser(userData);
            setBackend(newBackendConnection);
            if (!roles.includes(userData.role))
              roles = [...roles, userData.role];
            console.log('user registered:', roles);
          } else {
            console.log('user registration failed');
            setBackend(new LANS_API(''));
          }
        }

        setUserMetadata((previousMetadata) => {
          const newMetadata = {
            ...previousMetadata,
            roles,
            apiAccessToken,
          };
          return newMetadata;
        });
      } catch (e: any) {
        if (e.error !== 'consent_required') {
          console.log(e.message);
        }
      }
    };
    getUserMetadata();
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
