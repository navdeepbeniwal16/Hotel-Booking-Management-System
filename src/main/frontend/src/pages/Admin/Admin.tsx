import React, { useContext, useState, useEffect } from 'react';
import { useAuth0, withAuthenticationRequired } from '@auth0/auth0-react';

import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import Tab from 'react-bootstrap/Tab';
import Tabs from 'react-bootstrap/Tabs';

import endpoints from '../../api/endpoints';
import AppContext from '../../context/AppContext';

import Hotelier from '../../types/HotelierType';
import HotelGroupHotelier from '../../types/HotelGroupHotelier';
import { Roles } from '../../types/RoleTypes';

import HoteliersTable from './HoteliersTable';
import UsersTable from './UsersTable';
import UserDataType from '../../types/UserDataType';

const Admin = () => {
  const tabKeys = {
    users: 'Users',
    hotelGroups: 'Hotel Groups',
    hoteliers: 'Hoteliers',
    hotels: 'Hotels',
  };
  const { user, isLoading, isAuthenticated } = useAuth0();
  const {
    userMetadata: { apiAccessToken, roles },
  } = useContext(AppContext.GlobalContext);
  const [hoteliers, setHoteliers] = useState<Hotelier[]>([]);
  const [hotelGroupHoteliers, setHotelGroupHoteliers] = useState<
    Array<HotelGroupHotelier>
  >([]);
  const [tabKey, setTabKey] = useState(tabKeys.users);
  const [users, setUsers] = useState<UserDataType[]>([]);

  useEffect(() => {
    const getUsersOnLoad = async () => {
      const fetchedUsers = await endpoints.getAllUsers(apiAccessToken);
      setUsers(fetchedUsers);
    };
    getUsersOnLoad();
  }, []);

  const loadTab = async (tabKey: string) => {
    if (
      !isLoading &&
      isAuthenticated &&
      apiAccessToken !== '' &&
      roles.includes(Roles.ADMIN)
    ) {
      switch (tabKey) {
        case tabKeys.hoteliers:
          if (!hoteliers.length) {
            const fetchedHoteliers = await endpoints.getHoteliers(
              apiAccessToken
            );
            setHoteliers(fetchedHoteliers);
            const hgh = await endpoints.getHotelGroupHoteliers(apiAccessToken);
            setHotelGroupHoteliers(hgh);
          }
          break;
        case tabKeys.users:
          if (!users.length) {
            const fetchedUsers = await endpoints.getAllUsers(apiAccessToken);
            setUsers(fetchedUsers);
          }
          break;
        default:
      }
    }
    setTabKey(tabKey);
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
      ) : !(isAuthenticated && roles.includes(Roles.ADMIN)) ? (
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
            <Row>
              <Col>
                <UsersTable users={users} />
              </Col>
            </Row>
          </Tab>
          <Tab eventKey={tabKeys.hotelGroups} title={tabKeys.hotelGroups}>
            <p>{`${tabKeys.hotelGroups}`}</p>
          </Tab>
          <Tab eventKey={tabKeys.hoteliers} title={tabKeys.hoteliers}>
            <Row>
              <Col>
                <HoteliersTable
                  apiAccessToken={apiAccessToken}
                  hoteliers={hoteliers}
                  hotelGroupHoteliers={hotelGroupHoteliers}
                  setHoteliers={setHoteliers}
                  setHotelGroupHoteliers={setHotelGroupHoteliers}
                />
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
