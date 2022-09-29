import React from 'react';
import { map } from 'lodash';

import Table from 'react-bootstrap/Table';

import Hotelier from '../../types/HotelierType';

interface IHoteliersProps {
  hoteliers: Hotelier[];
}

const Hoteliers = ({ hoteliers }: IHoteliersProps) => {
  return (
    <Table>
      <thead>
        <tr>
          <th>User ID</th>
          <th>Email</th>
          <th>Name</th>
          <th>Hotel Group</th>
        </tr>
      </thead>
      <tbody>
        {map(hoteliers, (hotelier: Hotelier) => (
          <tr key={hotelier.id}>
            <td>{`${hotelier.id}`}</td>
            <td>{`${hotelier.email}`}</td>
            <td>{`${hotelier.name}`}</td>
            <td>{`${
              hotelier.hotel_group.name ? hotelier.hotel_group.name : '-'
            }`}</td>
          </tr>
        ))}
      </tbody>
    </Table>
  );
};

export default Hoteliers;
