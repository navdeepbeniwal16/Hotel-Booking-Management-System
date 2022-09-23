import React, { useContext, useState, useEffect } from 'react';
import { useAuth0, withAuthenticationRequired } from '@auth0/auth0-react';

import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';

import endpoints from '../api/endpoints';
import AppContext from '../context/AppContext';
import map from 'lodash/map';

type Hotelier = {
  email: string;
};

// const defaultHotelier = {
//   email: '',
// };

const Admin = () => {
  const { user, isLoading, isAuthenticated } = useAuth0();
  const { userMetadata } = useContext(AppContext.GlobalContext);
  const [hoteliers, setHoteliers] = useState<Hotelier[]>([]);

  useEffect(() => {
    const fetchHoteliers = async () => {
      const fetchedHoteliers = await endpoints.getHoteliers(
        userMetadata.apiAccessToken
      );
      setHoteliers(fetchedHoteliers);
    };

    if (!isLoading && isAuthenticated && userMetadata.apiAccessToken !== '') {
      fetchHoteliers();
    }
  }, [isLoading, isAuthenticated, userMetadata.apiAccessToken, setHoteliers]);

  return (
    <Container>
      <Row>
        <Col>
          <h1>Admin</h1>
        </Col>
      </Row>
      <Row>
        <Col>
          <p>
            {!isLoading && isAuthenticated
              ? user?.name || user?.email || 'Admin'
              : 'Not Allowed'}
          </p>
        </Col>
      </Row>
      <Row>
        <Col>
          <h2>Hoteliers</h2>
        </Col>
      </Row>
      <Row>
        <Col>
          {map(hoteliers, (hotelier: any) => (
            <div>`${hotelier}`</div>
          ))}
        </Col>
      </Row>
    </Container>
  );
};

export default withAuthenticationRequired(Admin, {
  // Show a message while the user waits to be redirected to the login page.
  onRedirecting: () => <div>Redirecting you to the login page...</div>,
});
