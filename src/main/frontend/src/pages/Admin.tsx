import React, { useContext, useState, useEffect } from 'react';
import { useAuth0, withAuthenticationRequired } from '@auth0/auth0-react';

import Table from 'react-bootstrap/Table';
import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import { Check2Circle, XCircle } from 'react-bootstrap-icons';

import endpoints from '../api/endpoints';
import AppContext from '../context/AppContext';
import map from 'lodash/map';
import Hotelier from '../types/HotelierType';

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

      <Row>
        <Col>
          <h2>Hoteliers</h2>
        </Col>
      </Row>
      <Row>
        <Col>
          <Table>
            <thead>
              <tr>
                <th>User ID</th>
                <th>Hotelier ID</th>
                <th>Active?</th>
                <th>Email</th>
                <th>Name</th>
                <th>Hotel Group</th>
              </tr>
            </thead>
            <tbody>
              {map(hoteliers, (hotelier: Hotelier) => (
                <tr key={hotelier.hotelier_id}>
                  <td>{`${hotelier.user_id}`}</td>
                  <td>{`${hotelier.hotelier_id}`}</td>
                  <td>{hotelier.isActive ? <Check2Circle /> : <XCircle />}</td>
                  <td>{`${hotelier.email}`}</td>
                  <td>{`${hotelier.name}`}</td>
                  <td>
                    {hotelier.hotel_group.name
                      ? `${hotelier.hotel_group.name}`
                      : 'N/A'}
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        </Col>
      </Row>
    </Container>
  );
};

export default withAuthenticationRequired(Admin, {
  // Show a message while the user waits to be redirected to the login page.
  onRedirecting: () => <div>Please wait: checking Admin authorisation</div>,
});
