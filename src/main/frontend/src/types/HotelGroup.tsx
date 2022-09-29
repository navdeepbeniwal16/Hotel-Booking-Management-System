import Address, { defaultAddress } from './AddressType';

type HotelGroup = {
  id: number;
  name?: string;
  phone?: string;
  address?: Address;
};

export const defaultHotelGroup: HotelGroup = {
  id: -1,
  name: 'Hotel Group',
  address: defaultAddress,
  phone: '-1',
};

export default HotelGroup;
