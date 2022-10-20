import React, { useEffect, useState, useContext } from 'react';

import AppContext from '../../context/AppContext';
import HotelGroup, {
  defaultHotelGroup,
  makeHotelGroup,
} from '../../types/HotelGroup';
import Hotel from '../../types/HotelType';
import { Row, Col, Container } from 'react-bootstrap';
import { useAuth0 } from '@auth0/auth0-react';
import GroupHotels from './GroupHotels';


const HotelierHome = () => {
  const {
    backend,
    user: { user },
  } = useContext(AppContext.GlobalContext);
  const noHotels: Array<Hotel> = [];
  const [hotels, setHotels] = useState(noHotels);
  const [group, setGroup] = useState(defaultHotelGroup);
  const { isAuthenticated } = useAuth0();
  useEffect(() => {
    const setup = async () => {
      backend
        .getHotelsForGroup(makeHotelGroup(user.group))
        .then((hotels: Hotel[]) => {
          setHotels(hotels);
        });
      backend.getHotelGroups().then((groups: HotelGroup[]) => {
        if (groups.length == 1) setGroup(groups[0]);
      });
    };
    if (user.id != -1 && isAuthenticated) setup();
  }, [user.id, isAuthenticated]);

  return (
    <Container fluid> 
      <Row>
        <Col><h3>{group.id != -1 && group.name ? `${group.name} hotels` : 'Hotels'}</h3></Col>
      </Row>
      <Row>
        <Col>
          <GroupHotels group={ group} hotels={hotels} />
        </Col>
      </Row>
    </Container>
  );
};

export default HotelierHome;
