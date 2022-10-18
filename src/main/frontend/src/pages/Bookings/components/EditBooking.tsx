import React, { useContext, useEffect, useState } from 'react';
import { Form, Card, Button } from 'react-bootstrap';
import BookingContext from '../context';
import { useNavigate, useParams } from 'react-router-dom';
import Booking, { defaultBooking } from '../../../types/BookingType';
import { map } from 'lodash';

const EditBooking = () => {
  const navigate = useNavigate();
  const { bookings, loading } = useContext(BookingContext);
  const { bookingId } = useParams();
  const [booking, setBooking] = useState(defaultBooking);
  const [originalStart, setOriginalStart] = useState(new Date(booking.start_date).toLocaleDateString())
  const [originalEnd, setOriginalEnd] = useState(new Date(booking.end_date).toLocaleDateString())

  useEffect(() => {
    const setup = async () => {
      map(bookings, (_booking: Booking) => {
        if (_booking.id == Number(bookingId)) setBooking(_booking);
        setOriginalStart(new Date(_booking.start_date).toLocaleDateString());
        setOriginalEnd(new Date(_booking.end_date).toLocaleDateString());
      });
    };
    setup();
  }, [bookingId, loading]);

  return (
    <Card>
      <Card.Header>
        <Card.Title>{booking.hotel_name}</Card.Title>
        <Card.Subtitle className="mb-2 text-muted">{originalStart} - {originalEnd}</Card.Subtitle>
      </Card.Header>
      <Form>
        <Card.Body>
          <Form.Group controlId='numGuests'>
            <Form.Label>Number of guests</Form.Label>
            <Form.Control type='number' placeholder={`0`} />
            <Form.Text className='text-muted'>
              How many guests are staying?
            </Form.Text>
          </Form.Group>

          <Form.Group controlId=''>
            <Form.Label>Check-in date</Form.Label>
            <Form.Control type='date' placeholder='' />
            <Form.Text className='text-muted'>
              What date are you checking in?
            </Form.Text>
          </Form.Group>

          <Form.Group controlId=''>
            <Form.Label>Check-out date</Form.Label>
            <Form.Control type='date' placeholder='' />
            <Form.Text className='text-muted'>
              What date are you checking out?
            </Form.Text>
          </Form.Group>

          <Form.Group>
            <Form.Label>Cancel your booking</Form.Label>
            <div>
              <Button variant='danger' onClick={() => console.log('cancel')}>
                Cancel booking
              </Button>
            </div>
            <Form.Text className='text-muted'>
              Warning: this action cannot be undone.
            </Form.Text>
          </Form.Group>
        </Card.Body>
        <Card.Footer className='d-flex justify-content-between'>
          <Button variant='secondary' onClick={() => navigate("/bookings")}>
            Back
          </Button>
          <Button variant='primary' className="mx-2" onClick={() => console.log('update')}>
            Update
          </Button>
        </Card.Footer>
      </Form>
    </Card>
  );
};

export default EditBooking;
