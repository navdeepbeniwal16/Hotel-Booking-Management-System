import Address, { defaultAddress } from './AddressType';

type HotelGroup = {
  id: number;
  name?: string;
  phone?: string;
  address?: Address;
};

export const defaultHotelGroup: HotelGroup = {
  id: -1,
  name: '',
  address: defaultAddress,
  phone: '+61412987456',
};

export default HotelGroup;
