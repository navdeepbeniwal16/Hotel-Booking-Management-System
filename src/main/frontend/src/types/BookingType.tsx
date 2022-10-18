import RoomBooking from "./RoomBooking";

type Booking = {
  id: number;
  hotel_id: number;
  customer_id?: number;
  customer_name: string;
  hotel_name: string;
  start_date: string;
  end_date: string;
  is_active: boolean;
  rooms?: RoomBooking[]
};

export const defaultBooking: Booking = {
  id: -1,
  hotel_id: -1,
  customer_id: -1,
  customer_name: '',
  hotel_name: '',
  start_date: '01/01/2022',
  end_date: '03/01/2022',
  is_active: false,
  rooms: []
}
export default Booking;
