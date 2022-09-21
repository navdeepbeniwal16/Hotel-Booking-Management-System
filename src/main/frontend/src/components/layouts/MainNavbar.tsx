import React, { ReactPropTypes } from 'react';
import Container from 'react-bootstrap/Container';
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import { useAuth0 } from '@auth0/auth0-react';

interface NavPropsType {
  username: String;
}

const MainNavbar = ({ username }: NavPropsType) => {
  const { loginWithRedirect, logout, isAuthenticated } = useAuth0();

  return (
    <Navbar bg='dark' variant='dark' expand='lg'>
      <Container>
        <Navbar.Toggle aria-controls='navbarScroll' />
        <Navbar.Collapse id='navbarScroll'>
          <Nav
            className='me-auto my-2 my-lg-0'
            style={{ maxHeight: '100px' }}
            navbarScroll
          >
            <Nav.Link href='/'>Home</Nav.Link>
            <Nav.Link href='/bookings'>Bookings</Nav.Link>
          </Nav>
          <Nav className='d-flex'>
            {isAuthenticated ? (
              <Nav.Link onClick={() => logout()}>Log out</Nav.Link>
            ) : (
              <>
                <Nav.Link onClick={() => loginWithRedirect()}>Log in</Nav.Link>
              </>
            )}
          </Nav>
        </Navbar.Collapse>
      </Container>
    </Navbar>
  );
};

export default MainNavbar;
