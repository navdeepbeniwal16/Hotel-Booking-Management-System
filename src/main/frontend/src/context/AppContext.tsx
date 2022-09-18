import React, { createContext, useState } from 'react';
import { ReactNode } from '@types/react';

type User = {
  username: string | null;
};

const defaultAppContext = {
  username: '',
};

const AppContext = createContext(defaultAppContext);

interface IAppProvider {
  children: ReactNode;
}

const AppProvider = ({ children }: IAppProvider) => {
  const [username, setUsername] = useState('');

  return (
    <AppContext.Provider value={{ username, setUsername }}>
      {children}
    </AppContext.Provider>
  );
};
