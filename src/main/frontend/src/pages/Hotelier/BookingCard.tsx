import React from 'react';
import { Button, ListGroupItem, Toast } from 'react-bootstrap';
import Booking from '../../types/BookingType';

interface BookingCardProps {
  booking: Booking;
  dark: boolean;
  onCancel: (booking: Booking) => void;
  showToast: number;
  onToastClose: () => void;
}

const BookingCard = ({ booking, dark, onCancel, showToast, onToastClose }: BookingCardProps) => {
  return (
    <ListGroupItem
      as='li'
      className='d-flex justify-content-between align-items-start'

      variant={dark ? 'dark':''}
    >
      <div className="ms-2 me-auto">
        <h4>{booking.customer_name}</h4>
        <div>
          <span className='fw-bold'>Booking ID: </span>
          {booking.id}
        </div>
        <div>
          <span className='fw-bold'>Start date: </span>
          {booking.start_date}
        </div>
        <div>
          <span className='fw-bold'>End date: </span>
          {booking.end_date}
        </div>
        <div>
          <span className='fw-bold'>Status: </span>
          {booking.is_active ? (
            <span className='fw-bold text-success'>Active</span>
          ) : (
            <span className='fw-bold text-danger'>Canceled</span>
          )}
        </div>
      </div>
      {booking.is_active ? <Button
        onClick={() => onCancel(booking)}
        variant='danger'
      >
        Cancel booking
      </Button> : <Toast show={showToast==booking.id} onClose={onToastClose} delay={3000} autohide>
        <Toast.Header>
          <strong className="me-auto">Booking cancelled</strong>
        </Toast.Header>
      </Toast>}
    </ListGroupItem>
  );
};

export default BookingCard;
