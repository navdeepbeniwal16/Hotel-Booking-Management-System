import React, { useContext } from 'react';
import Row from 'react-bootstrap/Row';
import { map } from 'lodash';
import {
  Container,
  Card,
  ListGroup,
  ListGroupItem,
  Button,
  Spinner,
} from 'react-bootstrap';
import Booking from '../../../types/BookingType';
import BookingContext from '../context';
import { useNavigate } from 'react-router-dom';

const BookingsList = () => {
  const navigate = useNavigate();
  const { bookings, loading } = useContext(BookingContext);

  const renderList = (): React.ReactNode => {
    return (
      <Container>
        <Row xs={1} md={1} className='g-4'>
          {
            loading && bookings.length == 0 ? <div>
              Loading your bookings<div>
                <Spinner animation="border" />
              </div>
            </div> : bookings.length == 0 && <div><h3>You do not have any bookings.</h3></div>
          }
          <ListGroup as='ul' variant='flush'>
            {map(bookings, (booking: Booking) => (
              <ListGroupItem as='li' key={booking.id}>
                <Card>
                  <Card.Header>
                    <Card.Title>{booking.hotel_name}</Card.Title>
                  </Card.Header>
                  <Card.Body>
                    <Card.Text>
                      <div>
                        <span className='fw-bold'>Booking ID: </span>
                        {booking.id}
                      </div>
                      <div>
                        <span className='fw-bold'>From: </span>
                        {booking.start_date}
                      </div>
                      <div>
                        <span className='fw-bold'>Until: </span>
                        {booking.end_date}
                      </div>
                      <div>
                        <span className='fw-bold'>Status: </span>
                        <span
                          className={
                            booking.is_active ? 'text-success' : 'text-danger'
                          }
                        >
                          {booking.is_active ? 'Active' : 'Cancelled'}
                        </span>
                      </div>
                    </Card.Text>
                  </Card.Body>
                  {booking.is_active && (
                    <Card.Footer>
                      <Button
                        variant='primary'
                        onClick={() => navigate(`${booking.id}`)}
                      >
                        Edit
                      </Button>
                    </Card.Footer>
                  )}
                </Card>
              </ListGroupItem>
            ))}
          </ListGroup>
        </Row>
      </Container>
    );
  };

  return <>{loading ? <Spinner animation='border' /> : renderList()}</>;
};

export default BookingsList;
