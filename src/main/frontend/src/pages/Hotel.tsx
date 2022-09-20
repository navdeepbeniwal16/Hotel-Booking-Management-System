import React, { useEffect } from 'react';

import AppContext from '../context/AppContext';
import { useContext, useState } from 'react';
import endpoints from '../api/endpoints';
import { Room, defaultRoom } from '../types/RoomType';
import { map } from 'lodash';

import Card from 'react-bootstrap/Card';
import Col from 'react-bootstrap/Col';
import Row from 'react-bootstrap/Row';

const Hotel = () => {
  const hotelState = useContext(AppContext.GlobalContext).hotel;
  const [rooms, setRooms] = useState([defaultRoom]);

  useEffect(() => {
    const fetchRooms = async () => {
      console.log('fetching rooms for hotel:', hotelState.hotel.hotel_id);
      const fetchedRooms: Room[] = await endpoints.getHotelRooms(
        hotelState.hotel.hotel_id
      );
      setRooms(fetchedRooms);
      console.log('fetched rooms:', rooms);
    };
    fetchRooms().catch(console.error);
  }, []);

  return (
    <>
      <h1>{hotelState.hotel.name}</h1>

      <div>
        <p>{hotelState.hotel.address}</p>
      </div>

      <h2>Rooms</h2>
      {map(rooms, (room: Room) => {
        return (
          <Row xs={1} md={1} className='g-4 mb-4'>
            <Col>
              <Card>
                <Card.Body>
                  <Card.Title>{room.type}</Card.Title>
                  <Card.Subtitle className='mb-2 text-muted'>
                    {`${room.room_id}`}
                  </Card.Subtitle>
                </Card.Body>
              </Card>
            </Col>
          </Row>
        );
      })}
    </>
  );
};

export default Hotel;
