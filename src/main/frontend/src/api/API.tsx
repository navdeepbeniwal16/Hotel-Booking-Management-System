import Hotel from '../types/HotelType';
import Hotelier from '../types/HotelierType';
import UserDataType, { defaultUserData } from '../types/UserDataType';
import HotelGroup from '../types/HotelGroup';
import Room from '../types/RoomType';
import Booking from '../types/BookingType';

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
  private isRegistered = false;

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
      return [true, user];
    }
    return [false, defaultUserData];
  }

  public async getAllUsers(): Promise<UserDataType[]> {
    const res = await fetch(this.usersEndpoint, {
      headers: this.headers,
    });
    const data = await res.json();
    const users: Array<UserDataType> = data.result.users;
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
  public async getAllRooms(hotel: Hotel): Promise<Room[]> {
    const res = await fetch('/api/rooms', {
      headers: this.headers,
      method: methods.POST,
      body: JSON.stringify({
        search: {
          hotel_id: hotel.id,
        },
      }),
    });
    const data = await res.json();
    const rooms: Array<Room> = data.result.rooms;
    console.log('getAllRooms():\n', rooms);
    return rooms;
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
  ): Promise<boolean> {
    const res = await fetch(this.hghEndpoint, {
      method: methods.DELETE,
      headers: this.headers,
      body: JSON.stringify({
        hotel_group_hotelier: { hotelier_id: hotelier_id },
      }),
    });
    const data = await res.json();
    const success: boolean = data.success;
    return success;
  }

  public async addToGroup(
    hotelier: Hotelier,
    group: HotelGroup
  ): Promise<boolean> {
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
    return success;
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
    return hotelGroups;
  }

  public async createGroup(group: HotelGroup): Promise<boolean> {
    if (group.id != -1) return false;
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
    const success: boolean = data.success;
    return success;
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
      console.log("ERROR getAllHotels:", data.errorMessage);
    }
    return hotels;
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

  public async createHotel(hotel: Hotel): Promise<boolean> {
    const res = await fetch(this.hotelsEndpoint, {
      method: methods.POST,
      headers: this.headers,
      body: JSON.stringify({
        hotel
      }),
    });
    const data = await res.json();
    const success: boolean = data.success;
    return success;
  }

  public async getHotelsForGroup(group: HotelGroup): Promise<Hotel[]> {
    const res = await fetch(this.hotelsEndpoint, {
      method: methods.POST,
      headers: this.headers,
      body: JSON.stringify({
        search: {
          hotels: ""
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
  public async getHotelBookings(hotel_id: number): Promise<Booking[]> {
    const body = JSON.stringify({
      search: {
        hotel_id,
      }
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
    } else {
      console.log("ERROR getHotelBookings:", data.errorMessage);
    }
    return bookings;
  }

  public async cancelBooking(booking: Booking): Promise<boolean> {
    const body = JSON.stringify({
      booking: {
        id: booking.id,
        cancel: true
      }
    });
    const res = await fetch(this.bookingsEndpoint, {
      method: methods.PUT,
      headers: this.headers,
      body,
    });
    const data = await res.json();
    if (res.ok && data.success) {
      return data.success;
    } else {
      console.log("ERROR cancelBooking:", data.errorMessage);
    }
    return false;
  }
}

export default LANS_API;
