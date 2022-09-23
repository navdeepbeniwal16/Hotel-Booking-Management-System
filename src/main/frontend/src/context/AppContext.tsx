import React, {
  Context,
  createContext,
  useState,
  ReactNode,
  useEffect,
  ReactPropTypes,
  useCallback,
} from 'react';
import { useAuth0 } from '@auth0/auth0-react';
import UserState from '../types/UserType';
import { defaultHotel, defaultHotelState } from '../types/HotelType';
import { defaultRoom, defaultRoomState } from '../types/RoomType';
import AppContextType from '../types/AppContextType';
import RoleT from '../types/RoleTypes';
import JwtT, { customClaims } from '../types/JwtType';
import jwtDecode from 'jwt-decode';

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
      try {
        apiAccessToken = await getAccessTokenSilently({
          audience: `${process.env.REACT_APP_AUTH0_AUDIENCE}`,
        });
      } catch (e: any) {
        if (e.error === 'consent_required') {
          apiAccessToken = await getAccessTokenWithPopup({
            audience: `${process.env.REACT_APP_AUTH0_AUDIENCE}`,
          });
        } else {
          console.log(e.message);
          throw e;
        }
      }

      const decodedToken: JwtT = jwtDecode(apiAccessToken);

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
  }, [isAuthenticated]);

  return (
    <GlobalContext.Provider
      value={{
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
  (props: ReactPropTypes, context: Context<AppContextType>) => {
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
