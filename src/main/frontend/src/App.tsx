import React, { ReactPropTypes, ContextType } from 'react';
import './App.css';

import 'bootstrap/dist/css/bootstrap.min.css';
import MainNavbar from './components/layouts/MainNavbar';

import Home from './pages/Home';
import Admin from './pages/Admin/Admin';
import Bookings from './pages/Bookings';
import Hotel from './pages/Hotel';
import { Route, Routes } from 'react-router-dom';
import { Container } from 'react-bootstrap';
import AppContext from './context/AppContext';
import { Auth0Provider } from '@auth0/auth0-react';

const App = (props: ReactPropTypes, context: ContextType<any>) => {
  const auth0 = {
    domain: process.env.REACT_APP_AUTH0_DOMAIN || '',
    childId: process.env.REACT_APP_AUTH0_CLIENTID || '',
    redirectUri: window.location.origin,
    audience: `${process.env.REACT_APP_AUTH0_AUDIENCE}` || '',
    // scope: 'read:current_user update:current_user_metadata',
  };

  return (
    <Auth0Provider
      domain={auth0.domain}
      clientId={auth0.childId}
      redirectUri={auth0.redirectUri}
      audience={auth0.audience}
      // scope={auth0.scope}
    >
      <AppContext.GlobalProvider>
        <div>
          <header>
            <MainNavbar />
          </header>
          <Container>
            <Routes>
              <Route path='/' element={<Home />}></Route>
              <Route path='/bookings' element={<Bookings />}></Route>
              <Route path='/hotel/:id' element={<Hotel />}></Route>
              <Route path='/admin' element={<Admin />}></Route>
            </Routes>
          </Container>
        </div>
      </AppContext.GlobalProvider>
    </Auth0Provider>
  );
};

export default App;
