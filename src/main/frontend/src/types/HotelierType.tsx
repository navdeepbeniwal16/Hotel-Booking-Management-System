import HotelGroup, { defaultHotelGroup } from './HotelGroup';

type Hotelier = {
  email: string;
  id: number;
  name: string;
  hotel_group: HotelGroup;
};

export const defaultHotelier: Hotelier = {
  email: 'default@hotelier.com',
  id: -1,
  name: 'Mr Default',
  hotel_group: defaultHotelGroup,
};

export default Hotelier;
