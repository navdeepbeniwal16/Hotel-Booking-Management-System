import React, { useContext, useEffect } from 'react';
import { useAuth0, withAuthenticationRequired } from '@auth0/auth0-react';
import AppContext from '../../context/AppContext';
import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import { Roles } from '../../types/RoleTypes';
import HotelierBody from './Body';

const HotelierPage = () => {
  const { user: auth0User, isLoading, isAuthenticated } = useAuth0();
  const {
    userMetadata: { roles },
    user: { user },
  } = useContext(AppContext.GlobalContext);
  // useEffect(() => {});

  const isAuthed = () => {
    return isAuthenticated && auth0User && roles.includes(Roles.HOTELIER);
  };

  const renderHeading = (): React.ReactNode => {
    return (
      <>
        {isAuthed() ? (
          <div>
            <h1>{auth0User?.name || auth0User?.email}</h1>
          </div>
        ) : (
          <h1>Hotelier Dashboard</h1>
        )}
      </>
    );
  };

  return (
    <Container>
      <Row>
        <Col>{renderHeading()}</Col>
      </Row>
      <Row>
        {isLoading ? (
          <h2>Loading</h2>
        ) : isAuthed() ? (
          <HotelierBody hotelier={user} />
        ) : (
          <h2>Not authorised</h2>
        )}
      </Row>
    </Container>
  );
};

// export default withAuthenticationRequired(HotelierPage, {
//   // Show a message while the user waits to be redirected to the login page.
//   onRedirecting: () => <div>Checking credentials</div>,
// });

export default HotelierPage;
