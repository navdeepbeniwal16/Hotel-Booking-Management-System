import React from 'react';
import { Button, ListGroupItem, Toast } from 'react-bootstrap';
import Booking from '../../types/BookingType';

interface BookingCardProps {
  booking: Booking;
  dark: boolean;
  onCancel: (booking: Booking) => void;
  showToast: number;
  onToastClose: () => void;
  toastError: string;
}

const BookingCard = ({
  booking,
  dark,
  onCancel,
  showToast,
  onToastClose,
  toastError,
}: BookingCardProps) => {
  return (
    <ListGroupItem
      as='li'
      variant={dark ? 'dark' : 'light'}
      className={`d-flex justify-content-between align-items-end`}
    >
      <div className='ms-2 me-auto'>
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
      {booking.is_active && showToast != booking.id && (
        <Button onClick={() => onCancel(booking)} variant='danger'>
          Cancel booking
        </Button>
      )}
      {
        <Toast
          show={showToast == booking.id}
          bg={showToast == booking.id && toastError ? 'danger' : 'success'}
          className={showToast == booking.id && toastError ? 'text-dark' : 'text-white'}
          onClose={onToastClose}
          delay={3000}
          autohide
        >
          <Toast.Body>
            <div>
              {showToast == booking.id && toastError ? (
                <Toast.Body>
                  <strong className='me-auto'>Something went wrong: </strong>{' '}
                  {toastError}
                </Toast.Body>
              ) : (
                showToast == booking.id && (
                  <Toast.Body>
                    <strong className='me-auto'>Success: </strong>
                    {booking.customer_name}
                    {"'"}s booking has been cancelled
                  </Toast.Body>
                )
              )}
            </div>
          </Toast.Body>
        </Toast>
      }
    </ListGroupItem>
  );
};

export default BookingCard;
