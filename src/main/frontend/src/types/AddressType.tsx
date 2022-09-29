import { filter } from 'lodash';

type Address = {
  line_1?: string;
  line_2?: string;
  city?: string;
  district?: string;
  postcode?: number;
};

export const defaultAddress: Address = {
  line_1: '100 Collins Street',
  line_2: '',
  city: 'Melbourne',
  district: 'VIC',
  postcode: 3000,
};

export const toString = (address: Address): string => {
  const nonEmptyFields = filter(
    Object.values(address),
    (field: string) => field.length > 0
  );
  return nonEmptyFields.join(', ');
};

export default Address;
