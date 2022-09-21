import React, { ReactNode, useEffect } from 'react';

import AppContext from '../context/AppContext';
import { useContext, useState } from 'react';
import endpoints from '../api/endpoints';
import { Room, defaultRoom } from '../types/RoomType';
import { map } from 'lodash';

import Card from 'react-bootstrap/Card';
import Col from 'react-bootstrap/Col';
import Row from 'react-bootstrap/Row';
import Button from 'react-bootstrap/Button';

const Hotel = () => {
  const hotelState = useContext(AppContext.GlobalContext).hotel;
  const [rooms, setRooms] = useState([defaultRoom]);

  useEffect(() => {
    const fetchRooms = async () => {
      console.log('fetching rooms for hotel:', hotelState.hotel.hotel_id);
      const fetchedRooms: Room[] = await endpoints.getAllRooms();
      setRooms(fetchedRooms);
      console.log('fetched rooms:', rooms);
    };
    fetchRooms().catch(console.error);
  }, []);

  const renderRoom = (room: Room): ReactNode => {
    if (room.hotel_id === hotelState.hotel.hotel_id) {
      return (
        <Row xs={1} md={1} className='g-4 mb-4'>
          <Col>
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
                  onClick={() => console.log('booked:', room.room_id)}
                  variant='primary'
                >
                  ${`${room.price}`}
                </Button>
              </Card.Body>
            </Card>
          </Col>
        </Row>
      );
    }
  };

  return (
    <>
      <h1>{hotelState.hotel.name}</h1>

      <div>
        <p>{hotelState.hotel.address}</p>
      </div>

      <h2>Rooms</h2>
      {map(rooms, (room: Room) => {
        return <div key={`${room.room_id}`}>{renderRoom(room)}</div>;
      })}
    </>
  );
};

export default Hotel;
