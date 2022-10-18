import React, { ReactNode, useEffect } from 'react';

import AppContext from '../../context/AppContext';
import { useContext, useState } from 'react';
import { Room, defaultRoom } from '../../types/RoomType';
import { filter, map } from 'lodash';
import {
  Container,
  Card,
  Form,
  Col,
  Row,
  Button,
  Spinner,
  Stack,
  Toast,
  ToastBody,
} from 'react-bootstrap';
import { toString } from '../../types/AddressType';
import { useNavigate, useParams } from 'react-router-dom';
import Hotel, { defaultHotel } from '../../types/HotelType';
import RoomBooking from '../../types/RoomBooking';

const HotelPage = () => {
  const navigate = useNavigate();
  const { backend } = useContext(AppContext.GlobalContext);
  const emptyRoom: Room[] = [];
  const [rooms, setRooms] = useState(emptyRoom);
  const [hotel, setHotel] = useState(defaultHotel);
  const { id } = useParams();
  const [loading, setLoading] = useState(false);
  const [startDate, setStartDate] = useState<Date>(new Date());
  const [endDate, setEndDate] = useState<Date>(new Date(2023, 11, 30));
  const emptyRoomBookings: RoomBooking[] = [];
  const [error, setError] = useState('');
  const [roomBookings, setRoomBookings] =
    useState<RoomBooking[]>(emptyRoomBookings);

  useEffect(() => {
    console.log('hotel: ', id);
    const setup = async () => {
      setLoading(true)
      backend.getHotel(Number(id)).then((_hotel: Hotel | undefined) => {
        setError('');
        console.log('hotel:', _hotel);
        if (_hotel) {
          setHotel(_hotel);
          console.log('hotel:', _hotel);
          backend
            .getAllRooms(_hotel, startDate, endDate)
            .then((_rooms: Room[]) => {
              setRooms(_rooms);
              setLoading(false);
              console.log('rooms:', rooms);
            });
        }
      });
    };
    setup();
  }, [id]);

  const handleAddRoomBooking = (room: Room): void => {
    const current = filter(
      roomBookings,
      (roomBooking: RoomBooking) => roomBooking.id == room.id
    );
    if (current.length == 0) {
      setRoomBookings([
        ...roomBookings,
        {
          id: room.id,
          no_of_guests: 0,
          main_guest_name: '',
          room,
        },
      ]);
      setRooms(filter(rooms, (_room: Room) => _room.id != room.id));
    }
  };
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
          <Card.Text>Price: ${`${room.price} / night`}</Card.Text>
          <Button onClick={() => handleAddRoomBooking(room)} variant='primary'>
            Add to booking
          </Button>
        </Card.Body>
      </Card>
    );
  };

  const handleNumGuests = (no_of_guests: number, roomBooking: RoomBooking) => {
    const { id } = roomBooking;
    const {occupancy} = roomBooking.room;
    setRoomBookings(
      map(roomBookings, (_roomBooking: RoomBooking) => {
        if (_roomBooking.id == id &&
          no_of_guests <= occupancy &&
          no_of_guests >= 0) {
          return { ..._roomBooking, no_of_guests };
        }
        return _roomBooking;
      })
    );
  };

  const handleGuestName = (main_guest_name: string, roomId: number) => {
    setRoomBookings(
      map(roomBookings, (_roomBooking: RoomBooking) => {
        if (_roomBooking.id == roomId) {
          return { ..._roomBooking, main_guest_name };
        }
        return _roomBooking;
      })
    );
  };
  const renderRoomBooking = (roomBooking: RoomBooking): ReactNode => {
    return (
      <Card>
        <Card.Header>
          <Card.Title>{roomBooking.room.type}</Card.Title>
          <Card.Subtitle>
            {`${roomBooking.room.occupancy}`} guests,{' '}
            {`${roomBooking.room.bed_type}`} bed
          </Card.Subtitle>
        </Card.Header>
        <Card.Body>
          <Card.Text>
            <Form.Group controlId={`${roomBooking.id}-guests`}>
              <Form.Label>Number of guests</Form.Label>
              <Form.Control
                required
                onChange={(event) => {
                  const n = Number(event.target.value);
                  handleNumGuests(n, roomBooking);
                }}
                value={roomBooking.no_of_guests}
                type='number'
                placeholder='1'
                aria-placeholder='1'
              />
              <Form.Text className='text-muted'>
                How many guests will be staying in this room?
              </Form.Text>
            </Form.Group>
            <Form.Group controlId={`${roomBooking.id}-mainGuest`}>
              <Form.Label>Primary guest name</Form.Label>
              <Form.Control
                required
                onChange={(event) => handleGuestName(event.target.value, roomBooking.id)}
                value={roomBooking.main_guest_name}
                type='text'
                placeholder='Guest name'
                aria-placeholder='Guest name'
              />
              <Form.Text className='text-muted'>
                Enter name of primary guest for this room
              </Form.Text>
            </Form.Group>
          </Card.Text>
        </Card.Body>
        <Card.Footer>
          <Button
            variant='warning'
            onClick={() => {
              setRoomBookings(
                filter(
                  roomBookings,
                  (rb: RoomBooking) => rb.room.id != roomBooking.room.id
                )
              );
              setRooms([...rooms, roomBooking.room]);
            }}
          >
            Remove room
          </Button>
        </Card.Footer>
      </Card>
    );
  };

  const handleDateSubmit = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    console.log('update dates:', event.currentTarget);
    setLoading(true);
    backend.getAllRooms(hotel, startDate, endDate).then((_rooms: Room[]) => {
      setRooms(_rooms);
      setRoomBookings(emptyRoomBookings);
      console.log('rooms:', rooms);
      setLoading(false);
    });
  };

  const onStartDateChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    console.log('start date:', event.target.value);
    setStartDate(new Date(event.target.value));
  };

  const onEndDateChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    console.log('end date:', event.target.value);
    setEndDate(new Date(event.target.value));
  };

  const handleBookingSubmit = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    console.log('booking submitted');
    backend
      .createBooking(hotel, startDate, endDate, roomBookings)
      .then(([success, message]: [boolean, string]) => {
        if (success) {
          navigate('/bookings');
        } else {
          setError(`Something went wrong: ${message}. Please try again.`);
          // setTimeout(() => {
          //   window.location.reload();
          // }, 2000);
        }
      });
  };

  const renderBooking = (): React.ReactNode => {
    return (
      <Form onSubmit={handleBookingSubmit}>
        {error != '' ? (
          <Toast bg='danger'>
            <ToastBody>{error}</ToastBody>
          </Toast>
        ) : (
          <Stack gap={3}>
            <Row>
              <Col className='d-flex flex-column align-content-stretch'>
                <Button
                  type='submit'
                  variant={roomBookings.length == 0 ? 'secondary' : 'success'}
                  disabled={roomBookings.length == 0}
                >
                  {roomBookings.length == 0 ? 'No rooms' : 'Book rooms'}
                </Button>
              </Col>
            </Row>
            <Row md='auto' className='g-4 mb-4'>
              {roomBookings.length == 0 ? (
                <p>You have not added any rooms to your booking</p>
              ) : (
                map(roomBookings, (roomBooking: RoomBooking) => (
                  <Col key={roomBooking.room.id}>
                    <div>{renderRoomBooking(roomBooking)}</div>
                  </Col>
                ))
              )}
            </Row>
          </Stack>
        )}
      </Form>
    );
  };

  const renderRooms = (): React.ReactNode => {
    return (
      <>
        {loading ? (
          <Col>
            <Spinner animation='border' />
          </Col>
        ) : rooms.length == 0 || rooms[0] == defaultRoom ? (
          <p>No rooms available</p>
        ) : (
          map(rooms, (room: Room) => (
            <Col key={room.id}>
              <div>{renderRoom(room)}</div>
            </Col>
          ))
        )}
      </>
    );
  };

  return (
    <Container>
      <h1>{hotel.name}</h1>

      <div>
        <p>{toString(hotel.address)}</p>
      </div>

      <h3>Dates</h3>
      <Form onSubmit={handleDateSubmit}>
        <Row sm={12} className='mb-3'>
          <Col sm={3}>
            <Form.Group>
              <Form.Label>From</Form.Label>
              <Form.Control
                required
                onChange={onStartDateChange}
                value={startDate.toISOString().split('T')[0]}
                type='date'
                placeholder='01/01/2021'
                aria-placeholder='01/01/2021'
              />
              <Form.Text className='text-muted'>Start date</Form.Text>
            </Form.Group>
          </Col>
          <Col sm={3}>
            <Form.Group>
              <Form.Label>To</Form.Label>
              <Form.Control
                required
                onChange={onEndDateChange}
                value={endDate.toISOString().split('T')[0]}
                type='date'
                placeholder='01/01/2021'
                aria-placeholder='01/01/2021'
              />
              <Form.Text className='text-muted'>End date</Form.Text>
            </Form.Group>
          </Col>
          <Col sm={2} className='d-flex justify-content-center flex-column'>
            <Button variant='primary' type='submit'>
              Search dates
            </Button>
          </Col>
        </Row>
      </Form>

      <Row>
        <Container>
          <Row sm={12}>
            <Col
              sm={3}
              className='bg-light border border-secondary rounded p-2'
            >
              <h2>Booking</h2>
              {renderBooking()}
            </Col>
            <Col sm={9}>
              <h2>Rooms</h2>
              <Row md='auto' className='g-4 mb-4'>
                {renderRooms()}
              </Row>
            </Col>
          </Row>
        </Container>
      </Row>
    </Container>
  );
};

export default HotelPage;
