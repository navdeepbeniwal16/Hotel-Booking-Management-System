import Hotel from '../types/HotelType';
import Hotelier from '../types/HotelierType';
import UserDataType, { defaultUserData } from '../types/UserDataType';
import HotelGroup from '../types/HotelGroup';
import Room from '../types/RoomType';
import Booking from '../types/BookingType';
import { find } from 'lodash';
import RoomBooking from '../types/RoomBooking';

type Headers = {
  'Content-Type': string;
  Authorization?: string;
};

type HttpMethod = 'GET' | 'POST' | 'PUT' | 'DELETE';

type HttpMethods = {
  GET: HttpMethod;
  POST: HttpMethod;
  PUT: HttpMethod;
  DELETE: HttpMethod;
};

const methods: HttpMethods = {
  GET: 'GET',
  POST: 'POST',
  PUT: 'PUT',
  DELETE: 'DELETE',
};

class LANS_API {
  private readonly accessToken: string;
  private readonly headers: Headers;
  private readonly baseURL = '/api/';
  private readonly contentType = 'application/json';
  private readonly usersEndpoint = this.baseURL + 'users';
  private readonly hotelsEndpoint = this.baseURL + 'hotels';
  private readonly groupsEndpoint = this.baseURL + 'hotelgroups';
  private readonly hghEndpoint = this.baseURL + 'hotelgrouphoteliers';
  private readonly roomsEndpoint = this.baseURL + 'rooms';
  private readonly bookingsEndpoint = this.baseURL + 'bookings';
  private readonly roomBookingsEndpoint = this.baseURL + 'roombookings';
  public isRegistered = false;

  constructor(accessToken = '') {
    this.accessToken = accessToken;
    this.headers = {
      'Content-Type': this.contentType,
    };
    if (accessToken !== '') {
      this.headers = {
        ...this.headers,
        Authorization: `Bearer ${this.accessToken}`,
      };
    }
  }

  public sameToken(other: string): boolean {
    return this.accessToken == other;
  }
  

  // Users
  public async register(): Promise<[boolean, UserDataType]> {
    const res = await fetch(this.usersEndpoint, {
      method: methods.POST,
      headers: this.headers,
      body: JSON.stringify({
        new: true,
      }),
    });
    const data = await res.json();
    if (res.status == 200) {
      const user: UserDataType = data;
      this.isRegistered = true;
      return [true, user];
    }
    return [false, defaultUserData];
  }

  public async getAllUsers(): Promise<UserDataType[]> {
    const res = await fetch(this.usersEndpoint, {
      headers: this.headers,
    });
    const data = await res.json(); 
    let users: Array<UserDataType> = [];
    if (res.ok && data.result.users) {
      users = data.result.users;
    }
    return users;
  }

  public async makeHotelier(user: UserDataType): Promise<boolean> {
    const res = await fetch(this.usersEndpoint, {
      method: methods.POST,
      headers: this.headers,
      body: JSON.stringify({
        hotelier: {
          id: user.id,
        },
      }),
    });
    const data = await res.json();
    const success: boolean = data.success ? data.success : false;
    return success;
  }

  // Rooms
  public async getAllRooms(hotel: Hotel, startDate: Date = new Date(2000, 1, 1), endDate: Date = new Date(2100, 11, 30)): Promise<[Room[], boolean, string]> {
    let rooms: Array<Room> = [];
    if (endDate <= startDate) return [rooms, false, "start date must come before end date"];
    const res = await fetch(this.roomsEndpoint, {
      headers: this.headers,
      method: methods.POST,
      body: JSON.stringify({
        search: {
          hotel_id: hotel.id,
          start_date: startDate.toLocaleString('en-GB'),
          end_date: endDate.toLocaleString('en-GB'),
        },
      }),
    });
    const data = await res.json();
    const { success } = data;
    const { error } = data;
    const { result } = data;
    if (res.ok && success && result.rooms) {
      rooms = result.rooms;
    } else {
      console.log('getAllRooms():', data.error);
    }
    
    return [rooms, success, error];
  }

  public async createRoom(room: Room): Promise<[boolean, string]> {
    const res = await fetch(this.roomsEndpoint, {
      headers: this.headers,
      method: methods.POST,
      body: JSON.stringify({
        room
      })
    })
    const data = await res.json();
    if (res.ok) {
      const { success }: {success: boolean} = data;
      return [success, "created"];
    } else {
      return [false, data.error || res.statusText];
    }
  }

  // Hoteliers
  public async getHoteliers(): Promise<Hotelier[]> {
    const res = await fetch(this.usersEndpoint, {
      method: methods.POST,
      headers: this.headers,
      body: JSON.stringify({
        search: { type: 'hotelier' },
      }),
    });
    const data = await res.json();
    const hoteliers: Array<Hotelier> = data.result.hoteliers;
    return hoteliers;
  }

  // HGH
  public async removeHotelierFromHotelGroup(
    hotelier_id: number
  ): Promise<[boolean, string]> {
    const res = await fetch(this.hghEndpoint, {
      method: methods.DELETE,
      headers: this.headers,
      body: JSON.stringify({
        hotel_group_hotelier: { hotelier_id: hotelier_id },
      }),
    });
    const data = await res.json();
    const success: boolean = data.success;
    const error: string = data.error;
    if (res.ok) {
      return [success, error];

    }
    return [false, error ? error : `${res.status} ${res.statusText}`];
  }

  public async addToGroup(
    hotelier: Hotelier,
    group: HotelGroup
  ): Promise<[boolean, string]> {
    const res = await fetch(this.hghEndpoint, {
      method: methods.POST,
      headers: this.headers,
      body: JSON.stringify({
        hotel_group_hotelier: {
          hotelier_id: hotelier.id,
          hotel_group_id: group.id,
        },
      }),
    });
    const data = await res.json();
    const success: boolean = data.success;
    const error: string = data.error;
    if (res.ok) {
      return [success, error];

    }
    return [false, error ? error : `${res.status} ${res.statusText}`];
    
  }

  // Hotel groups
  public async getHotelGroups(): Promise<HotelGroup[]> {
    const res = await fetch(this.groupsEndpoint, {
      headers: this.headers,
    });
    const data = await res.json();
    const success: boolean = data.success;
    let hotelGroups: Array<HotelGroup> = [];
    if (success && data.result && data.result.groups) {
      hotelGroups = data.result.groups;
    }
    console.log("getHotelGroups:", hotelGroups);
    return hotelGroups;
  }

  public async createGroup(group: HotelGroup): Promise<[boolean, string]> {
    if (group.id != -1) return [false, "cannot create hotel using an id"];
    const res = await fetch(this.groupsEndpoint, {
      method: methods.POST,
      headers: this.headers,
      body: JSON.stringify({
        hotel_group: {
          ...group,
        },
      }),
    });
    const data = await res.json();
    console.log(res.status, res.statusText)
    const success: boolean = data.success;
    const error: string = data.error;
    if (res.ok && success) {
      return [success, error]
    }
    return [false, `${res.status} ${res.statusText}`];
  }

  // Hotels
  public async getAllHotels(): Promise<Hotel[]> {
    const res = await fetch(this.hotelsEndpoint, {
      headers: this.headers,
    });
    const data = await res.json();
    let hotels: Array<Hotel> = [];
    if (data.success) {
      hotels = data.result.hotels;
    } else {
      console.log('ERROR getAllHotels:', data.errorMessage);
    }
    return hotels;
  }

  public async getHotel(id: number): Promise<Hotel|undefined> {
    const hotels = await this.getAllHotels();
    return find(hotels, (hotel: Hotel) => (hotel.id == id));
  }

  public async removeHotel(hotel: Hotel): Promise<boolean> {
    const res = await fetch(this.hotelsEndpoint, {
      method: methods.PUT,
      headers: this.headers,
      body: JSON.stringify({
        hotel: {
          id: hotel.id,
          is_active: false,
        },
      }),
    });
    const data = await res.json();
    const success: boolean = data.success;
    return success;
  }

  public async createHotel(hotel: Hotel): Promise<[boolean, string]> {
    const res = await fetch(this.hotelsEndpoint, {
      method: methods.POST,
      headers: this.headers,
      body: JSON.stringify({
        hotel,
      }),
    });
    const data = await res.json();
    if (res.ok && data.success) {
      return [data.success, data.error];
    } else {
      console.log('Error creating hotel', res.status, res.statusText, data);
    }
    return [data.success, data.error];
  }

  public async getHotelsForGroup(group: HotelGroup): Promise<Hotel[]> {
    const res = await fetch(this.hotelsEndpoint, {
      method: methods.POST,
      headers: this.headers,
      body: JSON.stringify({
        search: {
          hotels: '',
          group_id: group.id
        },
      }),
    });
    const data = await res.json();
    let hotels: Array<Hotel> = [];
    if (data.result && data.success) {
      hotels = data.result.hotels;
    }
    return hotels;
  }

  // Bookings
  public async getCustomerBookings(user: UserDataType): Promise<[Booking[], string]> {
    const res = await fetch(this.bookingsEndpoint, {
      method: methods.POST,
      headers: this.headers,
      body: JSON.stringify({
        search: {
          customer_bookings: user.id
        }
      })

    })
    let bookings: Booking[] = []
    let message = "";
    const data = await res.json();
    if (res.ok && data.result) {
      console.log("getCustomerBookings", data);
      const { result } = data;
      const success: boolean = data.success as boolean;
      if (success) {
        bookings = result.bookings;
      } else {
        message = result.error;
      }
    }
    console.log(res.status, res.statusText);
    console.log(data);
    return [bookings, message];
  }

  public async createBooking(hotel: Hotel,
    startDate: Date,
    endDate: Date,
    roomBookings: RoomBooking[]): Promise<[boolean, string]> {
    if (endDate <= startDate) return [false, "start date must come before end date"];
    const body = JSON.stringify({
      booking: {
        hotel_id: hotel.id,
        start_date: startDate.toLocaleString('en-GB').split(',')[0],
        end_date: endDate.toLocaleString('en-GB').split(',')[0],
        rooms: roomBookings
      }
    })
    const res = await fetch(this.bookingsEndpoint, {
      method: methods.POST,
      headers: this.headers,
      body
    })
    const data = await res.json();

    const { success }: { success: boolean } = data;
    const error = !success ? data.error : "";
    if (res.ok) {
      return [success, error];
    } else {
      console.log(res.status, res.statusText);
      return [false, error ? error : `${res.status} ${res.statusText}`];
    }
  }

  public async getHotelBookings(hotel_id: number): Promise<Booking[]> {
    const body = JSON.stringify({
      search: {
        hotel_id,
      },
    });
    const res = await fetch(this.bookingsEndpoint, {
      method: methods.POST,
      headers: this.headers,
      body,
    });
    const data = await res.json();
    let bookings: Array<Booking> = [];
    if (res.ok && data.result && data.success && data.result.bookings) {
      bookings = data.result.bookings;
      console.log("getHotelBookings", data);
    } else {
      console.log('ERROR getHotelBookings:', data.errorMessage);
    }
    return bookings;
  }

  public async cancelBooking(booking: Booking): Promise<[boolean, string]> {
    const body = JSON.stringify({
      booking: {
        ...booking,
        cancel: true,
      },
    });
    const res = await fetch(this.bookingsEndpoint, {
      method: methods.PUT,
      headers: this.headers,
      body,
    });
    const data = await res.json();
    if (res.ok) {
      return [data.success, data.error];
    } else {
      console.log('ERROR cancelBooking:', res.status, res.statusText, data);
    }
    const msg = data.error ? data.error : `${res.status} ${res.statusText}`
    return [false, msg];
  }

  public async changeBookingDates(booking: Booking, start: Date, end: Date): Promise<[boolean, string]> {
    if (end <= start) return [false, "start date must come before end date"];

    const body = JSON.stringify({
      booking: {
        ...booking,
        start_date: start.toLocaleString('en-GB').split(',')[0],
        end_date: end.toLocaleString('en-GB').split(',')[0],
      },
    });
    const res = await fetch(this.bookingsEndpoint, {
      method: methods.PUT,
      headers: this.headers,
      body,
    });
    const data = await res.json();
    if (res.ok && data.success) {
      return [data.success, data.error];
    } else {
      console.log('ERROR changing dates:', res.status, res.statusText, data);
    }
    return [data.success, data.error];
  }

  public async changeGuests(booking: Booking, roomBooking: RoomBooking): Promise<[boolean, string]> {
    console.log("changeGuests:");
    console.log(booking);
    console.log(roomBooking);
    const body = JSON.stringify({
      room_booking: {
        booking_id: booking.id,
        rb_id: roomBooking.id,
        no_of_guests: roomBooking.no_of_guests
      },
    });
    const res = await fetch(this.roomBookingsEndpoint, {
      method: methods.PUT,
      headers: this.headers,
      body,
    });
    const data = await res.json();
    if (res.ok && data.success) {
      return [data.success, data.error];
    } else {
      console.log('ERROR change number of guests for room booking:', res.status, res.statusText, data);
    }
    return [data.success, data.error];
  }
}

export default LANS_API;
