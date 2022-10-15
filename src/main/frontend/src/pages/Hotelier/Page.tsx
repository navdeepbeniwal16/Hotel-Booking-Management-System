import React, { useContext } from 'react';
import { useAuth0 } from '@auth0/auth0-react';
import AppContext from '../../context/AppContext';
import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import { Roles } from '../../types/RoleTypes';
import { Outlet } from 'react-router-dom';

const HotelierPage = () => {
  const { user: auth0User, isLoading, isAuthenticated } = useAuth0();
  const {
    userMetadata: { roles },
  } = useContext(AppContext.GlobalContext);
  // useEffect(() => {});

  const isAuthed = () => {
    return isAuthenticated && auth0User && roles.includes(Roles.HOTELIER);
  };

  const renderHeading = (): React.ReactNode => {
    return (
      <>
        <h1>Hotelier Dashboard</h1>
        {isAuthed() && (
          <div>
            <span className="fw-bold">Hotelier: </span>
            {auth0User?.name || auth0User?.email}
          </div>
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
          <Outlet />
        ) : (
          <h2>Not authorised</h2>
        )}
      </Row>
    </Container>
  );
};

export default HotelierPage;