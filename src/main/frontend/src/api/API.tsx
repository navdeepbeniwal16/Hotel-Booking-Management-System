import Hotel from '../types/HotelType';
import Room from '../types/RoomType';
import Hotelier from '../types/HotelierType';
import HotelGroupHotelier from '../types/HotelGroupHotelier';
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

  public async getAllUsers(): Promise<UserDataType[]> {
    const res = await fetch(this.usersEndpoint, {
      headers: this.headers,
    });
    const data = await res.json();
    const users: Array<UserDataType> = data.result.users;
    return users;
  }

  public async getHoteliers(): Promise<Hotelier[]> {
    const res = await fetch(this.usersEndpoint, {
      method: 'POST',
      headers: this.headers,
      body: JSON.stringify({
        search: { type: 'hotelier' },
      }),
    });
    const data = await res.json();
    console.log('getHoteliers:\n', data);
    const hoteliers: Array<Hotelier> = data.result.hoteliers;
    return hoteliers;
  }

  public async getHotelGroups(): Promise<HotelGroup[]> {
    const res = await fetch(this.hotelGroupsEndpoint, {
      headers: this.headers,
    });
    const data = await res.json();
    const hotelGroups: Array<HotelGroup> = data.result.hotel_groups;
    return hotelGroups;
  }
}

export default LANS_API;
