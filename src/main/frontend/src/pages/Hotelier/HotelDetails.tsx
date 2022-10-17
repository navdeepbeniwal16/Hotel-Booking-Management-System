import React, { useContext, useEffect, useState } from 'react';
import AppContext from '../../context/AppContext';
import { useParams } from 'react-router-dom';
import Booking from '../../types/BookingType';
import { map } from 'lodash';
import Hotel, { defaultHotel } from '../../types/HotelType';
import Room from '../../types/RoomType';
import { useAuth0 } from '@auth0/auth0-react';
import BookingCard from './BookingCard';
import {
  ListGroup,
  Tab,
  Card,
  Row,
  Col,
  Container,
  Nav,
  Stack,
} from 'react-bootstrap';
import { toString } from '../../types/AddressType';
import CreateRoom from './CreateRoom';

const HotelBookings = () => {
  const { isAuthenticated } = useAuth0();
  const { backend } = useContext(AppContext.GlobalContext);
  const hotelId = Number(useParams().hotelId);
  const defaultBookings: Array<Booking> = [];
  const [bookings, setBookings] = useState(defaultBookings);
  const [hotel, setHotel] = useState({ ...defaultHotel, id: hotelId });
  const defaultRooms: Array<Room> = [];
  const [rooms, setRooms] = useState(defaultRooms);

  useEffect(() => {
    const setup = async () => {
      if (hotelId) {
        backend.getHotelBookings(hotelId).then((_bookings: Booking[]) => {
          setBookings(_bookings);
        });
        backend.getAllHotels().then((hotels: Hotel[]) => {
          map(hotels, (_hotel: Hotel) => {
            if (_hotel.id == hotelId) {
              setHotel(_hotel);
            }
          });
        });
        backend.getAllRooms(hotel).then((_rooms: Room[]) => {
          setRooms(_rooms);
        });
      }
    };
    setup();
  }, [isAuthenticated]);

  const [toast, showToast] = useState(-1);

  const handleCancel = (booking: Booking) => {
    backend.cancelBooking(booking).then((success: boolean) => {
      if (success) {
        setBookings(
          map(bookings, (next: Booking) => {
            if (next.id == booking.id) {
              return { ...next, is_active: false };
            }
            return next;
          })
        );
        showToast(booking.id);
      } else {
        console.log('Error cancelling booking:', booking);
      }
    });
  };

  const handleToastClose = () => {
    showToast(-1);
  };

  return (
    <Container fluid>
      <Row>
        <Col>
          <Tab.Container defaultActiveKey='rooms' id='hotelTabs'>
            <Row className='g-3'>
              <Col sm={4}>
                <Stack gap={3}>
                  <Card className='p-2 border border-light'>
                    <Card.Title>
                      <h2>{hotel.name}</h2>
                    </Card.Title>
                    <Card.Subtitle className='mb-2'>
                      {toString(hotel.address)}
                    </Card.Subtitle>
                  </Card>
                  <Nav variant='pills' className='flex-column'>
                    <Nav.Item>
                      <Nav.Link eventKey='create'>New room</Nav.Link>
                    </Nav.Item>
                    <Nav.Item>
                      <Nav.Link eventKey='rooms'>Rooms</Nav.Link>
                    </Nav.Item>
                    <Nav.Item>
                      <Nav.Link eventKey='bookings'>Bookings</Nav.Link>
                    </Nav.Item>
                  </Nav>
                </Stack>
              </Col>
              <Col sm={8}>
                <Tab.Content>
                  <Tab.Pane eventKey='create' title='New Room'>
                    <CreateRoom hotel={hotel} />
                  </Tab.Pane>
                </Tab.Content>
                <Tab.Content>
                  <Tab.Pane eventKey='rooms' title='Rooms'>
                    <h3>Rooms</h3>
                      {rooms.length ? (
                        <ListGroup as='ul' variant='flush'>
                          {map(rooms, (room: Room, index: number) => {
                            return (
                              <Card key={index} className='mb-2' as='li'>
                                <Card.Header>
                                  <Card.Title>
                                    <span className='fw-bold'>{room.id}. </span>
                                    {room.type}
                                  </Card.Title>
                                </Card.Header>
                                <Card.Body>
                                  <div>
                                    <span className='fw-bold'>
                                      Max guests:{' '}
                                    </span>
                                    {room.occupancy}
                                  </div>
                                  <div>
                                    <span className='fw-bold'>Price: </span>$
                                    {room.price}
                                  </div>
                                </Card.Body>
                              </Card>
                            );
                          })}
                        </ListGroup>
                      ) : (
                        <div>No bookings for {hotel.name}</div>
                      )}
                  </Tab.Pane>
                </Tab.Content>
                <Tab.Content>
                  <Tab.Pane eventKey='bookings' title='Bookings'>
                    <h3>Bookings</h3>
                    {bookings.length ? (
                      <ListGroup as='ul' variant="flust">
                        {map(bookings, (booking: Booking, index: number) => {
                          return (
                            <BookingCard
                              booking={booking}
                              key={booking.id}
                              dark={index % 2 == 0}
                              onCancel={handleCancel}
                              showToast={toast}
                              onToastClose={handleToastClose}
                            />
                          );
                        })}
                      </ListGroup>
                    ) : (
                      <div>No bookings for {hotel.name}</div>
                    )}
                  </Tab.Pane>
                </Tab.Content>
              </Col>
            </Row>
          </Tab.Container>
        </Col>
      </Row>
    </Container>
  );
};

export default HotelBookings;
