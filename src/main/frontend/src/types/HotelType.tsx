import React, { Dispatch } from 'react';
import Address, { defaultAddress } from './AddressType';
type Hotel = {
  id: number;
  hotel_group_id: number;
  name: string;
  phone: string;
  email: string;
  address: Address;
};

export type HotelState = {
  hotel: Hotel;
  setHotel: Dispatch<React.SetStateAction<Hotel>>;
};

export const defaultHotel: Hotel = {
  id: -1,
  hotel_group_id: -1,
  name: 'default hotel',
  address: defaultAddress,
  phone: '123',
  email: 'hotel@default.com',
};

export const defaultHotelState: HotelState = {
  hotel: defaultHotel,
  setHotel: () =>
    console.error('Error: cannot call setHotel() without context'),
};

export default Hotel;
