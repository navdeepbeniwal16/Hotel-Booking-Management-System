import './App.css';

import 'bootstrap/dist/css/bootstrap.min.css';
import { MainNavbar } from './components/layouts/MainNavbar';

import { Home } from './pages/Home';
import { Bookings } from './pages/Bookings';
import { Route, Routes } from 'react-router-dom';
import { Container } from 'react-bootstrap';

import { useContext } from 'react';

function App() {
  return (
    <div>
      <header>
        <MainNavbar></MainNavbar>
      </header>
      <Container>
        <Routes>
          <Route path='/' element={<Home></Home>}></Route>
          <Route path='/bookings' element={<Bookings></Bookings>}></Route>
        </Routes>
      </Container>
    </div>
  );
}

export default App;
