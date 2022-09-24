import React from 'react';
import map from 'lodash/map';

import Table from 'react-bootstrap/Table';
import Button from 'react-bootstrap/Button';
import { Check2Circle, XCircle, DashCircle } from 'react-bootstrap-icons';

import Hotelier from '../../types/HotelierType';
import { Row, Col } from 'react-bootstrap';

interface IHoteliersProps {
  hoteliers: Hotelier[];
}

const Hoteliers = ({ hoteliers }: IHoteliersProps) => {
  const renderHotelGroupName = (hotel_group_name: string | undefined) => {
    if (hotel_group_name === undefined) return 'N/A';
    return (
      <div className='flex'>
        <p>{`${hotel_group_name}`}</p>
        <Button variant='outline-danger' size='sm'>
          <DashCircle />
        </Button>
      </div>
    );
  };
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
            <td>{renderHotelGroupName(hotelier.hotel_group.name)}</td>
          </tr>
        ))}
      </tbody>
    </Table>
  );
};

export default Hoteliers;
