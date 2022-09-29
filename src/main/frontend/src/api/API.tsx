import Hotel from '../types/HotelType';
import Hotelier from '../types/HotelierType';
import UserDataType from '../types/UserDataType';
import HotelGroup from '../types/HotelGroup';

type Headers = {
  'Content-Type': string;
  Authorization: string;
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

  constructor(accessToken = '') {
    this.accessToken = accessToken;
    this.headers = {
      'Content-Type': this.contentType,
      Authorization: `Bearer ${this.accessToken}`,
    };

    this.usersEndpoint = this.baseURL + 'users';
  }

  // Users
  public async getAllUsers(): Promise<UserDataType[]> {
    const res = await fetch(this.usersEndpoint, {
      headers: this.headers,
    });
    const data = await res.json();
    const users: Array<UserDataType> = data.result.users;
    return users;
  }

  // Hoteliers

  public async getHoteliers(): Promise<Hotelier[]> {
    const res = await fetch(this.usersEndpoint, {
      method: 'POST',
      headers: this.headers,
      body: JSON.stringify({
        search: { type: 'hotelier' },
      }),
    });
    const data = await res.json();
    const hoteliers: Array<Hotelier> = data.result.hoteliers;
    return hoteliers;
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
