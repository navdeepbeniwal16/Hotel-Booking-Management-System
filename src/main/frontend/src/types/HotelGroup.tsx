import Address, { defaultAddress } from './AddressType';

type HotelGroup = {
  id: number;
  name: string;
  phone?: string;
  address: Address;
};

export const defaultHotelGroup: HotelGroup = {
  id: -1,
  name: '',
  address: defaultAddress,
  phone: '+61412987456',
};

export const makeHotelGroup = (
  id = -1,
  name = '',
  phone = '',
  address = defaultAddress
): HotelGroup => {
  return { id, name, phone, address } as HotelGroup;
};

export default HotelGroup;
