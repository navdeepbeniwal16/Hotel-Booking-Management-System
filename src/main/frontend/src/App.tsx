import React, { ReactPropTypes, ContextType } from 'react';
import './App.css';

import 'bootstrap/dist/css/bootstrap.min.css';
import MainNavbar from './components/layouts/MainNavbar';

import Home from './pages/Home';
import Bookings from './pages/Bookings';
import Hotel from './pages/Hotel';
import { Route, Routes } from 'react-router-dom';
import { Container } from 'react-bootstrap';

import { useContext } from 'react';
import AppContext from './context/AppContext';

import { Auth0Provider } from '@auth0/auth0-react';

// dotenv.config();

const App = (props: ReactPropTypes, context: ContextType<any>) => {
  const { user } = useContext(AppContext.GlobalContext);

  const auth0 = {
    domain: process.env.REACT_APP_AUTH0_DOMAIN || '',
    childId: process.env.REACT_APP_AUTH0_CLIENTID || '',
    redirectUri: window.location.origin,
  };

  console.log(auth0);

  return (
    <Auth0Provider
      domain={auth0.domain}
      clientId={auth0.childId}
      redirectUri={auth0.redirectUri}
    >
      <AppContext.GlobalProvider>
        <div>
          <header>
            <MainNavbar username={user.username}></MainNavbar>
          </header>
          <Container>
            <Routes>
              <Route path='/' element={<Home />}></Route>
              <Route path='/bookings' element={<Bookings />}></Route>
              <Route path='/hotel/:id' element={<Hotel />}></Route>
            </Routes>
          </Container>
        </div>
      </AppContext.GlobalProvider>
    </Auth0Provider>
  );
};

export default App;
