import React, { Dispatch } from 'react';

type Hotel = {
  address: String;
  phone: String;
  name: String;
  id: Number;
  email: String;
};

export type HotelState = {
  hotel: Hotel;
  setHotel: Dispatch<React.SetStateAction<Hotel>>;
};

export const defaultHotel: Hotel = {
  address: '',
  phone: '',
  name: '',
  id: -1,
  email: '',
};

export const defaultHotelState: HotelState = {
  hotel: defaultHotel,
  setHotel: () =>
    console.error('Error: cannot call setHotel() without context'),
};

export default Hotel;
