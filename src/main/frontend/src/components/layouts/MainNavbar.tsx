import React, { ReactPropTypes } from 'react';
import Container from 'react-bootstrap/Container';
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';

interface NavPropsType {
  username: String;
}

const MainNavbar = ({ username }: NavPropsType) => {
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
            {username != '' ? (
              <Nav.Link href='#'>Log out</Nav.Link>
            ) : (
              <>
                <Nav.Link href='#'>Sign up</Nav.Link>
                <Nav.Link href='#'>Log in</Nav.Link>
              </>
            )}
          </Nav>
        </Navbar.Collapse>
      </Container>
    </Navbar>
  );
};

export default MainNavbar;
