import HotelGroup from './HotelGroup';

type Hotelier = {
  email: string;
  hotelier_id: number;
  isActive: boolean;
  name: string;
  user_id: number;
  hotel_group: HotelGroup;
};

export default Hotelier;
