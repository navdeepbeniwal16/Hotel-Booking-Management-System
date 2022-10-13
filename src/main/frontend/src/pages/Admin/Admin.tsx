import React, { useContext } from 'react';
import { useAuth0, withAuthenticationRequired } from '@auth0/auth0-react';

import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';

import AppContext from '../../context/AppContext';

import { Roles } from '../../types/RoleTypes';

import AdminTabs from './Tabs';

const Admin = () => {
  const { user, isLoading, isAuthenticated } = useAuth0();
  const {
    userMetadata: { roles },
  } = useContext(AppContext.GlobalContext);

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
        <div>
          <AdminTabs />
        </div>
      )}
    </Container>
  );
};

export default withAuthenticationRequired(Admin, {
  // Show a message while the user waits to be redirected to the login page.
  onRedirecting: () => <div>Please wait: checking Admin authorisation</div>,
});
