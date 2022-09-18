import React, {
  Context,
  createContext,
  useState,
  ReactNode,
  ReactPropTypes,
} from 'react';

import User from '../types/UserType';

const defaultUser: User = {
  username: '',
  setUsername: () =>
    console.error('Error: cannot call setUsername() without context'),
};

const defaultGlobalContext = {
  user: defaultUser,
};

const GlobalContext = createContext(defaultGlobalContext);

interface IGlobalProvider {
  children: ReactNode;
}

const GlobalProvider = ({ children }: IGlobalProvider) => {
  const [username, setUsername] = useState('');

  const user = {
    username,
    setUsername,
  };

  return (
    <GlobalContext.Provider value={{ user }}>{children}</GlobalContext.Provider>
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
