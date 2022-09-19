import React, { ReactPropTypes, ContextType, useEffect } from 'react';
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

const App = (props: ReactPropTypes, context: ContextType<any>) => {
  const { user } = useContext(AppContext.GlobalContext);

  return (
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
  );
};

export default App;
