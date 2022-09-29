import React from 'react';
import { map } from 'lodash';

import Table from 'react-bootstrap/Table';

import Hotel from '../../types/HotelType';
import { toString } from '../../types/AddressType';

interface IHotelsTableProps {
  hotels: Hotel[];
}

const Hotels = ({ hotels }: IHotelsTableProps) => {
  return (
    <Table>
      <thead>
        <tr>
          <th>ID</th>
          <th>Name</th>
          <th>Hotel Group</th>
          <th>Phone</th>
          <th>Address</th>
        </tr>
      </thead>
      <tbody>
        {map(hotels, (hotel: Hotel) => (
          <tr key={hotel.id}>
            <td>{`${hotel.id}`}</td>
            <td>{`${hotel.name}`}</td>
            <td>{`${hotel.hotel_group_id}`}</td>
            <td>{`${hotel.phone}`}</td>
            <td>{`${hotel.address ? toString(hotel.address) : '-'}`}</td>
          </tr>
        ))}
      </tbody>
    </Table>
  );
};

export default Hotels;
