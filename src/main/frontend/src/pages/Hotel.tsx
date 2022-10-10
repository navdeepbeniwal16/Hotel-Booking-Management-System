import React, { ReactNode, useEffect } from 'react';

import AppContext from '../context/AppContext';
import { useContext, useState } from 'react';
import { Room, defaultRoom } from '../types/RoomType';
import { map } from 'lodash';
import { useAuth0 } from '@auth0/auth0-react';
import Card from 'react-bootstrap/Card';
import Col from 'react-bootstrap/Col';
import Row from 'react-bootstrap/Row';
import Button from 'react-bootstrap/Button';
import { toString } from '../types/AddressType';

const Hotel = () => {
  const { loginWithRedirect, isAuthenticated } = useAuth0();
  const { hotel: hotelState, backend } = useContext(AppContext.GlobalContext);
  const [rooms, setRooms] = useState([defaultRoom]);

  useEffect(() => {
    const fetchRooms = async () => {
      const fetchedRooms: Room[] = await backend.getAllRooms(hotelState.hotel);
      setRooms(fetchedRooms);
    };
    fetchRooms().catch(console.error);
  }, [hotelState.hotel.id, rooms.length]);

  const renderRoom = (room: Room): ReactNode => {
    return (
      <Card style={{ width: '18rem' }}>
        <Card.Img
          variant='top'
          src={`https://via.placeholder.com/180x100?text=${hotelState.hotel.name}`}
        />
        <Card.Body>
          <Card.Title>{room.type}</Card.Title>
          <Card.Text>Guests: {`${room.occupancy}`}</Card.Text>
          <Card.Text>Bed: {`${room.bed_type}`}</Card.Text>
          <Button
            onClick={() => {
              if (!isAuthenticated) loginWithRedirect({});
            }}
            variant='primary'
          >
            ${`${room.price} / night`}
          </Button>
        </Card.Body>
      </Card>
    );
  };

  return (
    <>
      <h1>{hotelState.hotel.name}</h1>

      <div>
        <p>{toString(hotelState.hotel.address)}</p>
      </div>

      <h2>Rooms</h2>
      <Row md='auto' className='g-4 mb-4'>
        {rooms.length == 0 || rooms[0] == defaultRoom ? (
          <p>No rooms available</p>
        ) : (
          map(rooms, (room: Room) => (
            <Col>
              <div key={`${room.id}`}>{renderRoom(room)}</div>
            </Col>
          ))
        )}
      </Row>
    </>
  );
};

export default Hotel;
