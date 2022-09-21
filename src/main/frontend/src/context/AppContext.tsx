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

const defaultUser: UserState = {
  username: '',
  setUsername: () =>
    console.error('Error: cannot call setUsername() without context'),
};

type UserMetadata = {
  apiAccessToken: string;
};

const defaultUserMetadata: UserMetadata = {
  apiAccessToken: '',
};

const defaultGlobalContext = {
  user: defaultUser,
  hotel: defaultHotelState,
  room: defaultRoomState,
  userMetadata: defaultUserMetadata,
  getAccessToken: () => {
    console.log('getAccessToken not set');
  },
};

const GlobalContext = createContext(defaultGlobalContext);

interface IGlobalProvider {
  children: ReactNode;
}

const GlobalProvider = ({ children }: IGlobalProvider) => {
  const { user, getAccessTokenSilently, getAccessTokenWithPopup } = useAuth0();
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

  const getAccessToken = async () => {
    let apiAccessToken;
    try {
      apiAccessToken = await getAccessTokenSilently({
        audience: `${process.env.REACT_APP_AUTH0_AUDIENCE}`,
      });
    } catch (e: any) {
      if (e.error === 'consent_required') {
        console.log('getting consent');
        apiAccessToken = await getAccessTokenWithPopup({
          audience: `${process.env.REACT_APP_AUTH0_AUDIENCE}`,
        });
      } else {
        console.log(e.message);
        throw e;
      }
    }
    return apiAccessToken;
  };

  useEffect(() => {
    const getUserMetadata = async () => {
      let apiAccessToken = await getAccessToken();
      console.log('User access token:', apiAccessToken);
      setUserMetadata({ apiAccessToken });
    };

    getUserMetadata();
  }, []);

  return (
    <GlobalContext.Provider
      value={{
        user: userState,
        hotel: hotelState,
        room: roomState,
        userMetadata,
        getAccessToken,
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
