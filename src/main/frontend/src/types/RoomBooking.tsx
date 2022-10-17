import Room from "./RoomType";

type RoomBooking = {
  room_id: number,
  no_of_guests: number,
  main_guest_name: string,
  room: Room
}

export default RoomBooking;