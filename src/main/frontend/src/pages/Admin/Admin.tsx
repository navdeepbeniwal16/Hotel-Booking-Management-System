import React, { useContext, useState, useEffect } from 'react';
import { useAuth0, withAuthenticationRequired } from '@auth0/auth0-react';

import Table from 'react-bootstrap/Table';
import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import Tab from 'react-bootstrap/Tab';
import Tabs from 'react-bootstrap/Tabs';

import endpoints from '../../api/endpoints';
import AppContext from '../../context/AppContext';

import Hotelier from '../../types/HotelierType';
import HoteliersTable from './HoteliersTable';
import { Roles } from '../../types/RoleTypes';

const Admin = () => {
  const { user, isLoading, isAuthenticated } = useAuth0();
  const { userMetadata } = useContext(AppContext.GlobalContext);
  const [hoteliers, setHoteliers] = useState<Hotelier[]>([]);
  const [tabKey, setTabKey] = useState('Users');

  const loadTab = async (tabKey: string) => {
    if (
      !isLoading &&
      isAuthenticated &&
      userMetadata.apiAccessToken !== '' &&
      userMetadata.roles.includes(Roles.ADMIN)
    ) {
      switch (tabKey) {
        case 'Hoteliers':
          const fetchedHoteliers = await endpoints.getHoteliers(
            userMetadata.apiAccessToken
          );
          setHoteliers(fetchedHoteliers);
          break;
        default:
      }
    }
    setTabKey(tabKey);
  };

  const tabKeys = {
    users: 'Users',
    hotelGroups: 'Hotel Groups',
    hoteliers: 'Hoteliers',
    hotels: 'Hotels',
  };

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
      {isLoading ? (
        <h2>Loading</h2>
      ) : !(isAuthenticated && userMetadata.roles.includes(Roles.ADMIN)) ? (
        <h2>Not authorised</h2>
      ) : (
        <Tabs
          defaultActiveKey={tabKeys.users}
          id='admin-tabs'
          activeKey={tabKey}
          onSelect={(k) => loadTab(k || tabKeys.users)}
          className='mb-3'
        >
          <Tab eventKey={tabKeys.users} title={tabKeys.users}>
            <p>{`${tabKeys.users}`}</p>
          </Tab>
          <Tab eventKey={tabKeys.hotelGroups} title={tabKeys.hotelGroups}>
            <p>{`${tabKeys.hotelGroups}`}</p>
          </Tab>
          <Tab eventKey={tabKeys.hoteliers} title={tabKeys.hoteliers}>
            <Row>
              <Col>
                <HoteliersTable hoteliers={hoteliers} />
              </Col>
            </Row>
          </Tab>
          <Tab eventKey={tabKeys.hotels} title={tabKeys.hotels}>
            <p>{`${tabKeys.hotels}`}</p>
          </Tab>
        </Tabs>
      )}
    </Container>
  );
};

export default withAuthenticationRequired(Admin, {
  // Show a message while the user waits to be redirected to the login page.
  onRedirecting: () => <div>Please wait: checking Admin authorisation</div>,
});