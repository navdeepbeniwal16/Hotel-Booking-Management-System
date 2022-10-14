import React, { useState, useEffect, useContext } from 'react';
import Container from 'react-bootstrap/Container';
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import { useAuth0 } from '@auth0/auth0-react';
import { Roles } from '../../types/RoleTypes';
import AppContext from '../../context/AppContext';
import { useNavigate } from 'react-router-dom';

const MainNavbar = () => {
  const navigate = useNavigate();
  const { userMetadata } = useContext(AppContext.GlobalContext);
  const [isAdmin, setIsAdmin] = useState(false);
  const [isHotelier, setIsHotelier] = useState(false);

  const { user, loginWithRedirect, logout, isLoading, isAuthenticated } =
    useAuth0();

  useEffect(() => {
    setIsAdmin(userMetadata.roles.includes(Roles.ADMIN));
    setIsHotelier(userMetadata.roles.includes(Roles.HOTELIER));
  }, [isAuthenticated]);

  return (
    <Navbar bg='dark' variant='dark' expand='lg' className='mb-4'>
      <Container>
        <Navbar.Brand>
          <Nav.Link onClick={() => navigate('/')}>LANS Hotels</Nav.Link>
        </Navbar.Brand>
        <Navbar.Toggle aria-controls='navbarScroll' />
        <Navbar.Collapse id='navbarScroll'>
          <Nav
            className='me-auto my-2 my-lg-0'
            style={{ maxHeight: '100px' }}
            navbarScroll
          >
            <Nav.Link onClick={() => navigate('/')}>Home</Nav.Link>
            <Nav.Link onClick={() => navigate('/bookings')}>Bookings</Nav.Link>

            {isHotelier && (
              <Nav.Link onClick={() => navigate('/hotel')}>Admin</Nav.Link>
            )}

            {isAdmin && (
              <Nav.Link onClick={() => navigate('/admin')}>Admin</Nav.Link>
            )}
          </Nav>
          <Nav className='d-flex align-items-center'>
            {!isLoading && isAuthenticated && user ? (
              <>
                <Nav.Item className='text-light me-2'>
                  {user.name || user.email || 'User'}
                </Nav.Item>
                <Nav.Link
                  onClick={() => logout({ returnTo: window.location.origin })}
                >
                  Log out
                </Nav.Link>
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
