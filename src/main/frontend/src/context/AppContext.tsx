import React, {
  createContext,
  useState,
  ReactNode,
  Dispatch,
  ReactPropTypes,
} from 'react';

type EmptyString = '';

type User = {
  username: string | EmptyString;
  setUsername: Dispatch<React.SetStateAction<string>>;
};

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

  return (
    <GlobalContext.Provider value={{ user: { username, setUsername } }}>
      {children}
    </GlobalContext.Provider>
  );
};

const withGlobalContext =
  (Child: React.FC<ReactPropTypes>) => (props: ReactPropTypes) => {
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
