import React, { useContext, useState, useEffect } from 'react';
import { useAuth0, withAuthenticationRequired } from '@auth0/auth0-react';
import { map } from 'lodash';
import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import Tab from 'react-bootstrap/Tab';
import Tabs from 'react-bootstrap/Tabs';

import AppContext from '../../context/AppContext';

import Hotelier from '../../types/HotelierType';
import { Roles } from '../../types/RoleTypes';

import HoteliersTable from './HoteliersTable';
import HotelGroupsTable from './HotelGroupsTable';
import UsersTable from './UsersTable';
import UserDataType from '../../types/UserDataType';
import HotelGroup, { defaultHotelGroup } from '../../types/HotelGroup';
import Hotel from '../../types/HotelType';
import HotelTable from './HotelTable';

const Admin = () => {
  const tabKeys = {
    users: 'Users',
    hotelGroups: 'Hotel Groups',
    hoteliers: 'Hoteliers',
    hotels: 'Hotels',
  };
  const { user, isLoading, isAuthenticated } = useAuth0();
  const {
    backend,
    userMetadata: { apiAccessToken, roles },
  } = useContext(AppContext.GlobalContext);
  const [hotels, setHotels] = useState<Hotel[]>([]);
  const [hotelGroups, setHotelGroups] = useState<HotelGroup[]>([]);
  const [hoteliers, setHoteliers] = useState<Hotelier[]>([]);
  const [tabKey, setTabKey] = useState(tabKeys.users);
  const [users, setUsers] = useState<UserDataType[]>([]);

  useEffect(() => {
    const getUsersOnLoad = async () => {
      const fetchedUsers = await backend.getAllUsers();
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
        case tabKeys.users:
          if (!users.length) {
            const fetchedUsers = await backend.getAllUsers();
            setUsers(fetchedUsers);
          }
          break;
        case tabKeys.hotelGroups:
          if (!hotelGroups.length) {
            const fetchedHotelGroups = await backend.getHotelGroups();

            setHotelGroups(fetchedHotelGroups);
          }
          break;
        case tabKeys.hoteliers:
          if (hoteliers == undefined || !hoteliers.length) {
            const fetchedHoteliers = await backend.getHoteliers();
            setHoteliers(fetchedHoteliers);
          }
          break;
        case tabKeys.hotels:
          if (hotels == undefined || !hotels.length) {
            const fetchHotels = await backend.getAllHotels();
            setHotels(fetchHotels);
          }
          break;
        default:
      }
    }
    setTabKey(tabKey);
  };

  const handleRemoveHotelier = (hotelier_id: number): void => {
    const updatedHoteliers: Hotelier[] = map(
      hoteliers,
      (hotelier: Hotelier) => {
        if (hotelier.id == hotelier_id) {
          return {
            ...hotelier,
            hotel_group: defaultHotelGroup,
          };
        }
        return hotelier;
      }
    );
    setHoteliers(updatedHoteliers);
  };

  const handleMakeHotelier = (user_id: number): void => {
    setUsers(
      map(users, (user: UserDataType) => {
        if (user.id == user_id) {
          return {
            ...user,
            role: Roles.HOTELIER,
          };
        }
        return user;
      })
    );
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
            {!isLoading && isAuthenticated && user
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
                <UsersTable users={users} makeHotelier={handleMakeHotelier} />
              </Col>
            </Row>
          </Tab>
          <Tab eventKey={tabKeys.hotelGroups} title={tabKeys.hotelGroups}>
            <Row>
              <Col>
                <HotelGroupsTable hotel_groups={hotelGroups} />
              </Col>
            </Row>
          </Tab>
          <Tab eventKey={tabKeys.hoteliers} title={tabKeys.hoteliers}>
            <Row>
              <Col>
                <HoteliersTable
                  hoteliers={hoteliers}
                  removeHotelier={handleRemoveHotelier}
                />
              </Col>
            </Row>
          </Tab>
          <Tab eventKey={tabKeys.hotels} title={tabKeys.hotels}>
            <Row>
              <Col>
                <HotelTable hotels={hotels} />
              </Col>
            </Row>
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
