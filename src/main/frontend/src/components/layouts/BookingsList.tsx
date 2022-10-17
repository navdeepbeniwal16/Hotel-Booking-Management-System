import React from 'react';
import Row from 'react-bootstrap/Row';
import { map } from 'lodash';
import Booking from '../../types/BookingType';
import { Card, ListGroup, ListGroupItem, Button } from 'react-bootstrap';
interface IBookingListProps {
  bookings: Booking[];
}

const BookingsList = ({ bookings }: IBookingListProps) => {
  console.log('BookingList:', bookings);
  return (
    <div className='p-3'>
      <Row xs={1} md={1} className='g-4'>
        <h1>Your bookings</h1>
      </Row>
      <Row xs={1} md={1} className='g-4'>
        <ListGroup as="ul" variant="flush">
          {map(bookings, (booking: Booking) => (
            <ListGroupItem as="li" key={booking.id}>
              <Card>
                <Card.Header>
                  <Card.Title>{booking.hotel_name}</Card.Title>
                </Card.Header>
                <Card.Body>
                  <Card.Text>
                    <div><span className="fw-bold">From: </span>{booking.start_date}</div>
                    <div><span className="fw-bold">Until: </span>{booking.end_date}</div>
                    <div><span className="fw-bold">Status: </span>{booking.is_active ? "Active" : "Cancelled"}</div>
                  </Card.Text>
                </Card.Body>
                <Card.Footer>
                  <Button variant="danger" onClick={() => console.log("cancel:", booking.id)}>Cancel</Button>
                </Card.Footer>
              </Card>
            </ListGroupItem>
          ))}
        </ListGroup>
        
      </Row>
    </div>
  );
};

export default BookingsList;
