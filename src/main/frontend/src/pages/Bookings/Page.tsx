import React, { useEffect, useState, useContext } from 'react';

import { Outlet } from 'react-router-dom';
import { Container, Row, Col, Card } from 'react-bootstrap';
import Booking from '../../types/BookingType';
import AppContext from '../../context/AppContext';
import BookingContext from './context';
const BookingsPage = () => {
  const emptyBookings: Booking[] = [];
  const [bookings, setBookings] = useState(emptyBookings);
  const { backend, user } = useContext(AppContext.GlobalContext);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const setup = async () => {
      backend
        .getCustomerBookings(user.user)
        .then(([bookings, message]: [Booking[], string]) => {
          console.log('/bookings', bookings, message);
          setBookings(bookings);
          setLoading(false);
        });
    };
    setup();
  }, [user.user.id]);

  return (
    <BookingContext.Provider value={{bookings, loading, setLoading}}>
      <Container fluid>
        <Row xs={1} md={1} className='g-4'>
          <h1 className='display'>My bookings</h1>
        </Row>
        <Row xs={12}>
          <Col xs={3}>
            <Card>
              <Card.Body>
                <Card.Text>
                  <span className="fw-bold fs-6">User: </span>{user.user.email}
                </Card.Text>
              </Card.Body>
            </Card>
          </Col>
          <Col>
            <Outlet />
          </Col>
        </Row>
      </Container>
    </BookingContext.Provider>
  );
};

export default BookingsPage;
