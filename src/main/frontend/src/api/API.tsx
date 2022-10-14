import Hotel from '../types/HotelType';
import Hotelier from '../types/HotelierType';
import UserDataType from '../types/UserDataType';
import HotelGroup from '../types/HotelGroup';
import Room from '../types/RoomType';

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
  private readonly hotelGroupsEndpoint = this.baseURL + 'hotelgroups';
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
  public async triggerRegistration(): Promise<boolean> {
    const res = await fetch(this.usersEndpoint, {
      method: methods.POST,
      headers: this.headers,
      body: JSON.stringify({
        new: true,
      }),
    });
    const data = await res.json();
    if (res.status == 200) {
      const { success } = data;
      return success;
    }
    return false;
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
    const res = await fetch(this.hotelGroupsEndpoint, {
      headers: this.headers,
    });
    const data = await res.json();
    const hotelGroups: Array<HotelGroup> = data.result.hotel_groups;
    return hotelGroups;
  }

  public async createGroup(group: HotelGroup): Promise<boolean> {
    if (group.id != -1) return false;
    const res = await fetch(this.hghEndpoint, {
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
    const hotels: Array<Hotel> = data.result.hotels;
    return hotels;
  }
}

export default LANS_API;
