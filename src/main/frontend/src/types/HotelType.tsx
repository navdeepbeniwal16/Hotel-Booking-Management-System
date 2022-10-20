import React, { Dispatch } from 'react';
import Address, { defaultAddress, emptyAddress } from './AddressType';
import * as yup from 'yup';

type Hotel = {
  id: number;
  hotel_group_id: number;
  name: string;
  phone: string;
  email: string;
  address: Address;
  is_active?: boolean;
  pincode?: number;
  version?: number;
};

export type HotelState = {
  hotel: Hotel;
  setHotel: Dispatch<React.SetStateAction<Hotel>>;
};

export interface createHotelInterface {
  name: string,
  email: string,
  phone: string,
  address: {
    line_1: string,
    line_2?: string,
    city: string,
    district: string,
    postcode: number
  }
}

const createInitialValues: createHotelInterface = {
  name: '',
  email: '',
  phone: '',
  address: {
    line_1: '',
    line_2: '',
    city: '',
    district: '',
    postcode: 0
  }
}

const createSchema = yup.object().shape({
  name: yup.string().required(),
  email: yup.string().email().required(),
  phone: yup.string()
    .required()
    .test('valid_phone', 'Invalid phone', (value) => {
      if (value) {
        const re = /^\+?\d{0,11}$/g;
        return re.test(`${value}`)
      }
      return false;
    }),
  address: yup.object().shape({
    line_1: yup.string().required(),
    line_2: yup.string(),
    city: yup.string().required(),
    district: yup.string().required(),
    postcode: yup.number().test('valid_postcode', 'Invalid phone', (value) => {
      if (value) {
        const re = /^\d{4}$/g;
        return re.test(`${value}`)
      }
      return false;
    }).required()
  }).required()
});

export const schemas = {
  create: {
    schema: createSchema,
    initialValues: createInitialValues
  }
}

export const defaultHotel: Hotel = {
  id: -1,
  hotel_group_id: -1,
  name: 'default hotel',
  address: defaultAddress,
  phone: '123',
  email: 'hotel@default.com',
};

export const emptyHotel: Hotel = {
  id: -1,
  hotel_group_id: -1,
  name: '',
  address: emptyAddress,
  phone: '',
  email: '',
};

export const defaultHotelState: HotelState = {
  hotel: defaultHotel,
  setHotel: () =>
    console.error('Error: cannot call setHotel() without context'),
};

export default Hotel;
