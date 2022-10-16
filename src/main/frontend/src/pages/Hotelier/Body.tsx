import React, { useEffect, useState, useContext } from 'react';
import UserDataType from '../../types/UserDataType';

import AppContext from '../../context/AppContext';
import HotelGroup, {
  defaultHotelGroup,
  makeHotelGroup,
} from '../../types/HotelGroup';
import Hotel  from '../../types/HotelType';
import { Row, Col, Container } from 'react-bootstrap';
import GroupHotels from './GroupHotels';
import GroupDetails from './GroupDetails';
interface HotelierBody {
  hotelier: UserDataType;
}
const HotelierBody = () => {
  const {
    backend,
    user: { user },
  } = useContext(AppContext.GlobalContext);
  const [group, setGroup] = useState(defaultHotelGroup);
  const noHotels: Array<Hotel> = [];
  const [hotels, setHotels] = useState(noHotels);

  useEffect(() => {
    const setup = async () => {
      backend.getHotelGroups().then((groups: HotelGroup[]) => {
        if (groups.length == 1) setGroup(groups[0]);
      });
      backend
        .getHotelsForGroup(makeHotelGroup(user.group))
        .then((hotels: Hotel[]) => {
          setHotels(hotels);
        });
    };
    setup();
  }, [user]);

  return (
    <Container>
      <Row>
        <Col>
          <GroupDetails group={group} />
        </Col>
      </Row>
      <Row xs={1} className='g-4 mb-4'>
        <Col><GroupHotels hotels={hotels} /></Col>
      </Row>
    </Container>
  );
};

export default HotelierBody;
