import React, { useContext, useEffect, useState } from 'react';
import {
  Form,
  Stack,
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
  const [startDate, setStartDate] = useState<Date>(new Date());
  const [endDate, setEndDate] = useState<Date>(new Date(2023, 11, 30));
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  

  useEffect(() => {
    const setup = async () => {
      
      setError('');
      setSuccess('');
      map(bookings, (_booking: Booking) => {
        if (_booking.id == Number(bookingId)) {
          setBooking({..._booking});
        }
        setOriginalStart(new Date(_booking.start_date).toLocaleDateString());
        setOriginalEnd(new Date(_booking.end_date).toLocaleDateString());
      });
    };
    setup();
  }, [bookingId, loading]);
  useEffect(() => { console.log("EditBooking:", booking); }, [booking])

  const onStartDateChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    console.log('start date:', event.target.value);
    setStartDate(new Date(event.target.value));
  };

  const onEndDateChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    console.log('end date:', event.target.value);
    setEndDate(new Date(event.target.value));
  };

  const renderStartDate = (): React.ReactNode => {
    return (
      <Form.Group>
        <Form.Label>Check-in date</Form.Label>
        <Form.Control
          required
          onChange={onStartDateChange}
          value={startDate.toISOString().split('T')[0]}
          type='date'
          placeholder='01/01/2021'
          aria-placeholder='01/01/2021'
        />
        <Form.Text className='text-muted'>
          What date are you checking in?
        </Form.Text>
      </Form.Group>
    );
  };

  const renderEndDate = (): React.ReactNode => {
    return (
      <Form.Group controlId='endDAte'>
        <Form.Label>Check-out date</Form.Label>
        <Form.Control
          required
          onChange={onEndDateChange}
          value={endDate.toISOString().split('T')[0]}
          type='date'
          placeholder='01/01/2021'
          aria-placeholder='01/01/2021'
        />
        <Form.Text className='text-muted'>
          What date are you checking out?
        </Form.Text>
      </Form.Group>
    );
  };



  const handleChangeGuests = (event: React.FormEvent<HTMLFormElement>, roomBooking: RoomBooking) => {
    event.preventDefault();
    backend
      .changeGuests(booking, roomBooking)
      .then(([success, error]: [boolean, string]) => {
        if (success) {
          setError('');
          setSuccess(`Guests updated! Taking you back to your bookings now.`);
        } else {
          setSuccess('');
          setError(`Something went wrong: ${error}. Please try again.`);
        }
        setTimeout(() => {
          navigate('/bookings');
        }, 3000);
    });
  };

  const roomBooking = (
    index: number,
    roomBooking: RoomBooking
  ): React.ReactNode => {
    return (
      <Form onSubmit={(event) => handleChangeGuests(event, roomBooking)}>
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
                onChange={(event) => {
                  const rooms = map(booking.rooms, (rb: RoomBooking) => {
                    if (rb.id == roomBooking.id) {
                      return {
                        ...rb,
                        no_of_guests: Number(event.target.value),
                      };
                    }
                    return rb;
                  });
                  setBooking({ ...booking, rooms: rooms });
                }}
                value={roomBooking.no_of_guests}
                placeholder={`${roomBooking.no_of_guests}`}
              />
              <Form.Text className='text-muted'>
                How many guests are staying?
              </Form.Text>
            </Form.Group>
          </Card.Body>
          <Card.Footer className='d-flex justify-content-end'>
            <Button
              variant='primary'
              type="submit"
            >
              Update room
            </Button>
          </Card.Footer>
        </Card>
      </Form>
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
            Cancel
          </Button>
        </div>
        <Form.Text className='text-danger'>
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

  const updateButton = (): React.ReactNode => {
    return (
      <>
        <Button variant='primary' className='mx-2' type='submit'>
          Update book
        </Button>
      </>
    );
  };

  const handleChangeDates = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    backend
      .changeDates(booking, startDate, endDate)
      .then(([success, error]: [boolean, string]) => {
        if (success) {
          setError('');
          setSuccess(`Dates updated! Taking you back to your bookings now.`);
        } else {
          setSuccess('');
          setError(`Something went wrong: ${error}. Please try again.`);
        }
        setTimeout(() => {
          navigate('/bookings');
        }, 3000);
      });
  };

  const handleCancelBooking = () => {
    backend.cancelBooking(booking).then((success: boolean) => {
      if (success) {
        setError('');
        setSuccess(`Booking cancelled! Taking you back to your bookings now.`);
      } else {
        setSuccess('');
        setError(
          `Something went wrong: cancellation failed. Please try again.`
        );
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
            {error && (
              <Button
                variant='outline-light'
                onClick={() => navigate('/bookings')}
              >
                Back to my bookings
              </Button>
            )}
          </div>
        </Toast.Body>
      </Toast>
    );
  };

  return (
    <>
      <Card>
        <Card.Header>
          <Card.Title>{booking.hotel_name}</Card.Title>
          <Card.Subtitle className='mb-2 text-muted'>
            {originalStart} - {originalEnd}
          </Card.Subtitle>
        </Card.Header>

        <Card.Body>
          {!error && !success && (
            <Stack gap={3}>
              <Container>
                <Row>
                  <Col>
                    <Form onSubmit={handleChangeDates}>
                      <Card>
                        <Card.Body>
                          {renderStartDate()}
                          {renderEndDate()}
                        </Card.Body>
                        <Card.Footer className='d-flex justify-content-end'>
                          {updateButton()}
                        </Card.Footer>
                      </Card>
                    </Form>
                  </Col>
                </Row>
              </Container>

              <Container>
                <Row>
                  <Col>
                    <CardGroup>{rooms()}</CardGroup>
                  </Col>
                </Row>
              </Container>
              <Container>
                <Row>
                  <Col>
                    <Form>
                      <Card className='border-danger'>
                        <Card.Body>{cancel()}</Card.Body>
                      </Card>
                    </Form>
                  </Col>
                </Row>
              </Container>
            </Stack>
          )}
          {toast()}
        </Card.Body>
        <Card.Footer className='d-flex justify-content-start'>
          {backButton()}
        </Card.Footer>
      </Card>
    </>
  );
};

export default EditBooking;
