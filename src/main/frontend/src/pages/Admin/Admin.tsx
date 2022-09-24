import React, { useContext, useState, useEffect } from 'react';
import { useAuth0, withAuthenticationRequired } from '@auth0/auth0-react';

import Table from 'react-bootstrap/Table';
import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import Tab from 'react-bootstrap/Tab';
import Tabs from 'react-bootstrap/Tabs';
import { Check2Circle, XCircle } from 'react-bootstrap-icons';

import endpoints from '../../api/endpoints';
import AppContext from '../../context/AppContext';

import Hotelier from '../../types/HotelierType';
import HoteliersTable from './HoteliersTable';

const Admin = () => {
  const { user, isLoading, isAuthenticated } = useAuth0();
  const { userMetadata } = useContext(AppContext.GlobalContext);
  const [hoteliers, setHoteliers] = useState<Hotelier[]>([]);

  useEffect(() => {
    const fetchHoteliers = async () => {
      if (hoteliers.length) return;
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
      <Tabs defaultActiveKey='Users' id='admin-tabs' className='mb-3'>
        <Tab eventKey='Users' title='Users'>
          <p>Users</p>
        </Tab>
        <Tab eventKey='Hoteliers' title='Hoteliers'>
          <Row>
            <Col>
              <HoteliersTable hoteliers={hoteliers} />
            </Col>
          </Row>
        </Tab>
        <Tab eventKey='Hotels' title='Hotels'>
          <p>Hotels</p>
        </Tab>
      </Tabs>
    </Container>
  );
};

export default withAuthenticationRequired(Admin, {
  // Show a message while the user waits to be redirected to the login page.
  onRedirecting: () => <div>Please wait: checking Admin authorisation</div>,
});
