type HotelGroup = {
  id: number;
  name?: string;
  address?: string;
  phone?: string;
};

export const defaultHotelGroup: HotelGroup = {
  id: -1,
  name: 'Hotel Group',
  address: '123 Default Street',
  phone: '-1',
};

export default HotelGroup;
