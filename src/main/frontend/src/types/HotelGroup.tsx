type HotelGroup = {
  id: number;
  name?: string;
  address?: {
    line_1?: string;
    line_2?: string;
    city?: string;
    district?: string;
    postcode?: number;
  };
  phone?: string;
};

export const defaultHotelGroup: HotelGroup = {
  id: -1,
  name: 'Hotel Group',
  address: {
    line_1: '100 Collins Street',
    city: 'Melbourne',
    district: 'VIC',
    postcode: 3000,
  },
  phone: '-1',
};

export default HotelGroup;
