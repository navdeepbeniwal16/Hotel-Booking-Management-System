import React, { useContext, useEffect, useState } from 'react';
import {
  Form,
  Card,
  Button,
  Container,
  Row,
  Col,
  Toast,
  CardGroup,
} from 'react-bootstrap';
import BookingContext from '../context';
import { useNavigate, useParams } from 'react-router-dom';
import Booking, { defaultBooking } from '../../../types/BookingType';
import { map } from 'lodash';
import Room from '../../../types/RoomType';
import RoomBooking from '../../../types/RoomBooking';
import AppContext from '../../../context/AppContext';

const EditBooking = () => {
  const { backend } = useContext(AppContext.GlobalContext);
  const navigate = useNavigate();
  const { bookings, loading } = useContext(BookingContext);
  const { bookingId } = useParams();
  const [booking, setBooking] = useState(defaultBooking);
  const [originalStart, setOriginalStart] = useState(
    new Date(booking.start_date).toLocaleDateString()
  );
  const [originalEnd, setOriginalEnd] = useState(
    new Date(booking.end_date).toLocaleDateString()
  );
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  useEffect(() => {
    const setup = async () => {
      setError('');
      setSuccess('');
      map(bookings, (_booking: Booking) => {
        if (_booking.id == Number(bookingId)) setBooking(_booking);
        setOriginalStart(new Date(_booking.start_date).toLocaleDateString());
        setOriginalEnd(new Date(_booking.end_date).toLocaleDateString());
      });
    };
    setup();
  }, [bookingId, loading]);

  const startDate = (): React.ReactNode => {
    return (
      <Form.Group controlId=''>
        <Form.Label>Check-in date</Form.Label>
        <Form.Control type='date' placeholder='' />
        <Form.Text className='text-muted'>
          What date are you checking in?
        </Form.Text>
      </Form.Group>
    );
  };

  const endDate = (): React.ReactNode => {
    return (
      <Form.Group controlId=''>
        <Form.Label>Check-out date</Form.Label>
        <Form.Control type='date' placeholder='' />
        <Form.Text className='text-muted'>
          What date are you checking out?
        </Form.Text>
      </Form.Group>
    );
  };

  const roomBooking = (
    index: number,
    roomBooking: RoomBooking
  ): React.ReactNode => {
    return (
      <Card>
        <Card.Header>
          <Card.Title>Room #{index}</Card.Title>
        </Card.Header>
        <Card.Body>
          <Form.Group controlId={`${roomBooking.id}-main-guest`}>
            <Form.Label>Main guest:</Form.Label>
            <Form.Control
              disabled
              readOnly
              value={roomBooking.main_guest_name}
              type='text'
              placeholder='Name of main guest'
            />
          </Form.Group>
          <Form.Group controlId={`${roomBooking.id}-guests`}>
            <Form.Label>Number of guests:</Form.Label>
            <Form.Control
              type='number'
              placeholder={`${roomBooking.no_of_guests}`}
            />
            <Form.Text className='text-muted'>
              How many guests are staying?
            </Form.Text>
          </Form.Group>
        </Card.Body>
      </Card>
    );
  };

  const rooms = (): React.ReactNode => {
    return (
      <>
        {map(booking.rooms, (room: RoomBooking, index: number) =>
          roomBooking(index + 1, room)
        )}
      </>
    );
  };

  const cancel = (): React.ReactNode => {
    return (
      <Form.Group>
        <Form.Label>Cancel your booking</Form.Label>
        <div>
          <Button variant='danger' onClick={() => handleCancelBooking()}>
            Cancel booking
          </Button>
        </div>
        <Form.Text className='text-muted'>
          Warning: this action cannot be undone.
        </Form.Text>
      </Form.Group>
    );
  };

  const backButton = (): React.ReactNode => {
    return (
      <Button variant='secondary' onClick={() => navigate('/bookings')}>
        My bookings
      </Button>
    );
  };




  const buttons = (): React.ReactNode => {
    return (
      <>
        {backButton()}
        <Button
          variant='primary'
          className='mx-2'
          onClick={() => console.log('update')}
        >
          Update
        </Button>
      </>
    );
  };

  const handleChangeDates = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    // console.log('booking submitted');
    // backend
    //   .createBooking(hotel, startDate, endDate, roomBookings)
    //   .then(([success, message]: [boolean, string]) => {
    //     if (success) {
    //       navigate('/bookings');
    //     } else {
    //       setError(`Something went wrong: ${message}. Please try again.`);
    //       // setTimeout(() => {
    //       //   window.location.reload();
    //       // }, 2000);
    //     }
    //   });
  };

  const handleCancelBooking = () => {
    backend.cancelBooking(booking).then((success: boolean) => {
      if (success) {
        setError('');
        setSuccess(`Booking cancelled! Taking you back to your bookings now.`);
      } else {
        setSuccess('');
        setError(`Something went wrong: cancellation failed. Please try again.`);
      }
      setTimeout(() => {
        navigate('/bookings');
      }, 3000);
    });
  };

  const toast = (): React.ReactNode => {
    return (
      <Toast
        show={error != '' || success != ''}
        bg={error ? 'danger' : success && 'success'}
      >
        <Toast.Body>
          {error ? error : success && success}
          <div>
            {error && <Button variant="outline-light" onClick={() => navigate('/bookings')}>Back to my bookings</Button>}
          </div>
        </Toast.Body>
      </Toast>
    );
  };
  return (
    <Card>
      <Card.Header>
        <Card.Title>{booking.hotel_name}</Card.Title>
        <Card.Subtitle className='mb-2 text-muted'>
          {originalStart} - {originalEnd}
        </Card.Subtitle>
      </Card.Header>

      <Card.Body>
        {!error && !success && (
          <>
            <Form>
              {startDate()}
              {endDate()}
              {cancel()}
            </Form>
            <Container>
              <Row>
                <Col>
                  <Form>
                    <CardGroup>{rooms()}</CardGroup>
                  </Form>
                </Col>
              </Row>
            </Container>
          </>
        )}
        {toast()}
      </Card.Body>
      <Card.Footer className='d-flex justify-content-between'>
        {buttons()}
      </Card.Footer>
    </Card>
  );
};

export default EditBooking;
