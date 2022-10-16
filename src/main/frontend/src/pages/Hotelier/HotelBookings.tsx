import React, { useContext, useEffect, useState } from 'react';
import AppContext from '../../context/AppContext';
import { useParams } from 'react-router-dom';
import Booking from '../../types/BookingType';
import {  map } from 'lodash';
import Hotel, { defaultHotel } from '../../types/HotelType';
import { useAuth0 } from '@auth0/auth0-react';
import BookingCard from './BookingCard';
import { ListGroup} from 'react-bootstrap';

const HotelBookings = () => {
  const { isAuthenticated } = useAuth0();
  const { backend } = useContext(AppContext.GlobalContext);
  const hotelId = Number(useParams().hotelId);
  const defaultBookings: Array<Booking> = [];
  const [bookings, setBookings] = useState(defaultBookings);
  const [hotel, setHotel] = useState(defaultHotel);
  
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
      }
    };
    setup();
  }, [isAuthenticated]);


  const [toast, showToast] = useState(-1);

  const handleCancel = (booking: Booking) => {
    backend.cancelBooking(booking).then((success: boolean) => {
      if (success) {
        setBookings(map(bookings, (next: Booking) => {
          if (next.id == booking.id) {
            return { ...next, is_active: false }
          }
          return next;
        }));
        showToast(booking.id)
      } else {
        console.log("Error cancelling booking:", booking);
      }
    })
  }

  const handleToastClose = () => {
    showToast(-1);
  }

  return (
    <>
      <div className='my-2'>
        <h2>{hotel.name}</h2>
      </div>
      <h3>Bookings</h3>

      {bookings.length ? (
        <ListGroup as='ul' variant='flush'>
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
    </>
  );
};

export default HotelBookings;
