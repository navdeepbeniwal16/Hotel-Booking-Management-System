import React, { useEffect, useState, useContext } from 'react';
import UserDataType from '../../types/UserDataType';
import { toString as addressToString } from '../../types/AddressType';
import AppContext from '../../context/AppContext';
import HotelGroup, {
  defaultHotelGroup,
  makeHotelGroup,
} from '../../types/HotelGroup';
import Hotel from '../../types/HotelType';
import { Button, Row, Col, Card, ListGroup } from 'react-bootstrap';
import HotelsList from '../../components/molecules/HotelsList';
import { map } from 'lodash';
import { PlusCircle } from 'react-bootstrap-icons'

interface HotelierBody {
  hotelier: UserDataType;
}
const HotelierBody = ({ hotelier }: HotelierBody) => {
  const [group, setGroup] = useState(defaultHotelGroup);
  const noHotels: Array<Hotel> = [];
  const [hotels, setHotels] = useState(noHotels);
  const { backend } = useContext(AppContext.GlobalContext);
  useEffect(() => {
    const setup = async () => {
      backend.getHotelGroups().then((groups: HotelGroup[]) => {
        if (groups.length == 1) setGroup(groups[0]);
      });
      backend
        .getHotelsForGroup(makeHotelGroup(hotelier.group))
        .then((hotels: Hotel[]) => {
          setHotels(hotels);
        });
    };
    setup();
  }, [hotelier]);
  return (
    <>
      <div>
        <h3>Group details</h3>
        <div><span className='fw-bold'>Name: </span>{group.name}</div>
        <div><span className='fw-bold'>Phone: </span>{group.phone}</div>
        <div><span className='fw-bold'>Address: </span>{`${addressToString(group.address)}`}</div>
      </div>
      <div>
        <h3>Hotels</h3>
        <Row className='my-2'>
          <Col>
            <Button
              variant='info'
              onClick={() => console.log('create a new hotel')}
            >
              Create hotel <PlusCircle />
            </Button>
          </Col>
        </Row>
        <Row xs={1} className='g-4 mb-4'>
          <Col>
            <ListGroup as='ul'>
              {map(hotels, (hotel: Hotel) => {
                return (
                  <ListGroup.Item
                    as='li'
                    key={`${hotel.id}`}
                    className="d-flex justify-content-between align-items-start">
                    <div className='ms-2 me-auto' >
                      <div className='fw-bold'>{hotel.name}</div>
                      {addressToString(hotel.address)}
                    </div>
                    <Button
                      variant='primary'
                      onClick={() => console.log('create a new hotel')}
                    >
                      {' '}
                      View bookings{' '} 
                    </Button>
                  </ListGroup.Item>
                );
              })}
            </ListGroup>
          </Col>
        </Row>
      </div>
    </>
  );
};

export default HotelierBody;
