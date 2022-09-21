import React, { ReactPropTypes } from 'react';
import Container from 'react-bootstrap/Container';
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import { useAuth0 } from '@auth0/auth0-react';

interface NavPropsType {
  username: String;
}

const MainNavbar = ({ username }: NavPropsType) => {
  const { user, loginWithRedirect, logout, isLoading, isAuthenticated } =
    useAuth0();

  return (
    <Navbar bg='dark' variant='dark' expand='lg' className='mb-4'>
      <Container>
        <Navbar.Brand href='/'>LANS Hotels</Navbar.Brand>
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
          <Nav className='d-flex align-items-center'>
            {!isLoading && isAuthenticated && user ? (
              <>
                <Nav.Item className='text-light me-2'>
                  {user.name || user.email || 'User'}
                </Nav.Item>
                <Nav.Link onClick={() => logout()}>Log out</Nav.Link>
              </>
            ) : null}
            {isLoading ? (
              <Nav.Item className='text-light text-sm me-2'>
                Loading user
              </Nav.Item>
            ) : null}
            {!isLoading && !isAuthenticated ? (
              <Nav.Link onClick={() => loginWithRedirect()}>Log in</Nav.Link>
            ) : null}
          </Nav>
        </Navbar.Collapse>
      </Container>
    </Navbar>
  );
};

export default MainNavbar;
