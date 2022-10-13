import React, { useContext, useState, useEffect } from 'react';
import { useAuth0 } from '@auth0/auth0-react';
import { map } from 'lodash';
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
import { Spinner } from 'react-bootstrap';

const AdminTabs = () => {
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

  const loadHotelGroups = async () => {
    if (!hotelGroups.length) {
      const fetchedHotelGroups = await backend.getHotelGroups();

      setHotelGroups(fetchedHotelGroups);
    }
  };

  const loadHoteliers = async () => {
    if (hoteliers == undefined || !hoteliers.length) {
      const fetchedHoteliers = await backend.getHoteliers();
      setHoteliers(fetchedHoteliers);
    }
  };

  const loadHotels = async () => {
    if (hotels == undefined || !hotels.length) {
      const fetchHotels = await backend.getAllHotels();
      setHotels(fetchHotels);
    }
  };
  const loadUsers = async () => {
    if (!users.length) {
      const fetchedUsers = await backend.getAllUsers();
      setUsers(fetchedUsers);
    }
  };
  const loadTab = async (tabKey: string) => {
    if (
      !isLoading &&
      isAuthenticated &&
      apiAccessToken !== '' &&
      roles.includes(Roles.ADMIN)
    ) {
      switch (tabKey) {
        case tabKeys.users:
          loadUsers();
          break;
        case tabKeys.hotelGroups:
          loadHotelGroups();
          break;
        case tabKeys.hoteliers:
          loadHoteliers();
          loadHotelGroups();
          break;
        case tabKeys.hotels:
          loadHotels();
          break;
        default:
      }
    }
    setTabKey(tabKey);
  };

  const handleUpdateHotelier = (
    hotelier_id: number,
    hotelier: Hotelier
  ): void => {
    const updatedHoteliers: Hotelier[] = map(hoteliers, (next: Hotelier) => {
      if (next.id == hotelier_id) {
        // const msg = `Updated hotelier: ${next.name} (Group=${next.hotel_group.id}) => ${hotelier.name} (Group=${hotelier.hotel_group.id})`;
        return hotelier;
      }
      return next;
    });
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
    <>
      {isLoading ? (
        <h2>
          <Spinner animation='border' />
        </h2>
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
              <Col>Create hotel group</Col>
            </Row>
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
                  removeHotelier={(hotelier: Hotelier) =>
                    handleUpdateHotelier(hotelier.id, {
                      ...hotelier,
                      hotel_group: defaultHotelGroup,
                    })
                  }
                  addHotelierToGroup={(hotelier: Hotelier, group: HotelGroup) =>
                    handleUpdateHotelier(hotelier.id, {
                      ...hotelier,
                      hotel_group: group,
                    })
                  }
                  hotel_groups={hotelGroups}
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
    </>
  );
};

export default AdminTabs;
