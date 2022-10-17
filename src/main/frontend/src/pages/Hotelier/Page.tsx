import React, { useContext, useState, useEffect } from 'react';
import { useAuth0 } from '@auth0/auth0-react';
import AppContext from '../../context/AppContext';
import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import { Roles } from '../../types/RoleTypes';
import { Outlet } from 'react-router-dom';
import { Card, Stack, Breadcrumb } from 'react-bootstrap';
import GroupDetails from './GroupDetails';
import HotelGroup, { defaultHotelGroup } from '../../types/HotelGroup';
import { useLocation } from 'react-router-dom';

const HotelierPage = () => {
  const { user: auth0User, isLoading, isAuthenticated } = useAuth0();
  const [group, setGroup] = useState(defaultHotelGroup);
  const { pathname } = useLocation();

  const {
    backend,
    user: {user},
    userMetadata: { roles },
  } = useContext(AppContext.GlobalContext);

  useEffect(() => {
    const setup = async () => {
      backend.getHotelGroups().then((groups: HotelGroup[]) => {
        if (groups.length == 1) {
          setGroup(groups[0]);
          console.log('group set:', group.name);
        }
      });
    };
    if (user.id != -1 && isAuthenticated) setup();
  }, [user.id, isAuthenticated]);

  const isAuthed = () => {
    return isAuthenticated && auth0User && roles.includes(Roles.HOTELIER);
  };

  const hotelierDetails = (): React.ReactNode => {
    return (
      <>
        {isAuthed() && (
          <Card>
            <Card.Header>
              <h4>Hotelier</h4>
            </Card.Header>
            <Card.Body>
              <span className='fw-bold'>Email: </span>
              {isAuthed()
                ? auth0User?.name || auth0User?.email
                : 'Unauthorised'}
            </Card.Body>
          </Card>
        )}
      </>
    );
  };

  const breadCrumbs = () => {
    const home = /^(\/hotelier)$/g.test(pathname);
    const hotel = /^(\/hotelier\/)\d+$/g.test(pathname);
    return (
      <>
        {hotel && (
          <Breadcrumb>
            <Breadcrumb.Item href='/hotelier' active={home}>
              {group.name} group
            </Breadcrumb.Item>
            <Breadcrumb.Item active={hotel}>Hotel</Breadcrumb.Item>
          </Breadcrumb>
        )}
      </>
    );
  };
  return (
    <Container className='gx-4'>
      <Row sm={12}>
        <Col sm={4}>
          <Container>
            <Row>
              <Col>
                <Stack gap={3}>
                  <h1>Hotelier Dashboard</h1>
                  {hotelierDetails()}
                  {isAuthed() && <GroupDetails group={group} />}
                </Stack>
              </Col>
            </Row>
          </Container>
        </Col>
        <Col sm={8} className='bg-light border border-secondary rounded p-3'>
          {breadCrumbs()}

          {isLoading ? (
            <h2>Loading</h2>
          ) : isAuthed() ? (
            <Outlet />
          ) : (
            <h2>Not authorised</h2>
          )}
        </Col>
      </Row>
    </Container>
  );
};

export default HotelierPage;
