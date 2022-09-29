import React from 'react';
import { map } from 'lodash';

import Table from 'react-bootstrap/Table';

import HotelGroup from '../../types/HotelGroup';
import { toString } from '../../types/AddressType';

interface IHotelGroupProps {
  hotel_groups: HotelGroup[];
}

const HotelGroups = ({ hotel_groups }: IHotelGroupProps) => {
  return (
    <Table>
      <thead>
        <tr>
          <th>ID</th>
          <th>Name</th>
          <th>Phone</th>
          <th>Address</th>
        </tr>
      </thead>
      <tbody>
        {map(hotel_groups, (hotel_group: HotelGroup) => (
          <tr key={hotel_group.id}>
            <td>{`${hotel_group.id}`}</td>
            <td>{`${hotel_group.name}`}</td>
            <td>{`${hotel_group.phone}`}</td>
            <td>{`${
              hotel_group.address ? toString(hotel_group.address) : '-'
            }`}</td>
          </tr>
        ))}
      </tbody>
    </Table>
  );
};

export default HotelGroups;
