import React from 'react';
import map from 'lodash/map';

import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import Table from 'react-bootstrap/Table';
import { Check2Circle, XCircle } from 'react-bootstrap-icons';

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
          <th>Hotelier ID</th>
          <th>Active?</th>
          <th>Email</th>
          <th>Name</th>
          <th>Hotel Group</th>
        </tr>
      </thead>
      <tbody>
        {map(hoteliers, (hotelier: Hotelier) => (
          <tr key={hotelier.hotelier_id}>
            <td>{`${hotelier.user_id}`}</td>
            <td>{`${hotelier.hotelier_id}`}</td>
            <td>{hotelier.isActive ? <Check2Circle /> : <XCircle />}</td>
            <td>{`${hotelier.email}`}</td>
            <td>{`${hotelier.name}`}</td>
            <td>
              {hotelier.hotel_group.name
                ? `${hotelier.hotel_group.name}`
                : 'N/A'}
            </td>
          </tr>
        ))}
      </tbody>
    </Table>
  );
};

export default Hoteliers;
