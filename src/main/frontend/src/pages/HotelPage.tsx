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
import { useParams } from 'react-router-dom';
import Hotel, { defaultHotel } from '../types/HotelType';

const HotelPage = () => {
  const { loginWithRedirect, isAuthenticated } = useAuth0();
  const { backend } = useContext(AppContext.GlobalContext);
  const emptyRoom: Room[] = [];
  const [rooms, setRooms] = useState(emptyRoom);
  const [hotel, setHotel] = useState(defaultHotel);
  const { id } = useParams();
  const [loaded, setLoaded] = useState(false);
  const [startDate, setStartDate] = useState(new Date(2000, 1, 1));
  const [endDate, setEndDate] = useState(new Date(2100, 11, 30));
  useEffect(() => {
    console.log("hotel: ", id)
    const setup = async () => {
      backend.getHotel(Number(id)).then((_hotel: Hotel | undefined) => {
        console.log("hotel:", _hotel)
        if (_hotel) {
          setHotel(_hotel);
          console.log("hotel:", _hotel)
          backend.getAllRooms(_hotel, startDate, endDate).then((_rooms: Room[]) => {
            setRooms(_rooms);
            setLoaded(true);
            console.log("rooms:", rooms)
          })
        }
      })
    }
    setup();
  }, [id]);

  const renderRoom = (room: Room): ReactNode => {
    return (
      <Card style={{ width: '18rem' }}>
        <Card.Img
          variant='top'
          src={`https://via.placeholder.com/180x100?text=${hotel.name}`}
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
      <h1>{hotel.name}</h1>

      <div>
        <p>{toString(hotel.address)}</p>
      </div>

      <h2>Rooms</h2>
      <Row md='auto' className='g-4 mb-4'>
        {rooms.length == 0 || rooms[0] == defaultRoom ? (
          <p>No rooms available</p>
        ) : (
          map(rooms, (room: Room) => (
            <Col key={room.id}>
              <div>{renderRoom(room)}</div>
            </Col>
          ))
        )}
      </Row>
    </>
  );
};

export default HotelPage;
