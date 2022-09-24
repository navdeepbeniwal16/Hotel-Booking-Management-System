import React, { Dispatch, SetStateAction } from 'react';
import { map, filter } from 'lodash';

import Table from 'react-bootstrap/Table';
import Button from 'react-bootstrap/Button';
import {
  Check2Circle,
  XCircle,
  DashCircle,
  PlusCircle,
} from 'react-bootstrap-icons';

import Hotelier from '../../types/HotelierType';
import HotelGroup from '../../types/HotelGroup';
import HotelGroupHotelier from '../../types/HotelGroupHotelier';

import { Row, Col } from 'react-bootstrap';
import endpoints from '../../api/endpoints';

interface IHoteliersProps {
  apiAccessToken: string;
  hoteliers: Hotelier[];
  hotelGroupHoteliers: Array<HotelGroupHotelier>;
  setHoteliers: Dispatch<SetStateAction<Hotelier[]>>;
  setHotelGroupHoteliers: Dispatch<SetStateAction<HotelGroupHotelier[]>>;
}

const Hoteliers = ({
  apiAccessToken,
  hoteliers,
  hotelGroupHoteliers,
  setHoteliers,
  setHotelGroupHoteliers,
}: IHoteliersProps) => {
  const handleRemoveHotelier = async (
    remove_hotelier_id: number,
    remove_hotel_group_id: number
  ) => {
    const hotelierHotelGroupId = filter(
      hotelGroupHoteliers,
      (hgh: HotelGroupHotelier) =>
        hgh.hotelier_id === remove_hotelier_id &&
        hgh.hotel_group_id === remove_hotel_group_id
    );
    if (hotelierHotelGroupId.length === 1) {
      console.log(
        `Removing Hotelier(${remove_hotelier_id}) from Hotel Group(${remove_hotel_group_id})`
      );
      const res = await endpoints.removeHotelierFromHotelGroup(
        apiAccessToken,
        hotelierHotelGroupId[0].id
      );
      const data = await res.json();
      console.log(
        `Response: DELETE Hotelier(${remove_hotelier_id}) from Hotel Group(${remove_hotel_group_id})\n\t`,
        data
      );
      if (data.result.deleted) {
        setHoteliers((_) => {
          return map(hoteliers, (hotelier) => {
            if (hotelier.hotelier_id === remove_hotelier_id)
              return {
                ...hotelier,
                hotel_group: { id: -1 },
              };
            return hotelier;
          });
        });
        setHotelGroupHoteliers((_) => {
          return filter(
            hotelGroupHoteliers,
            (hgh) =>
              hgh.hotelier_id !== remove_hotelier_id &&
              hgh.hotel_group_id !== remove_hotel_group_id
          );
        });
        console.log(
          `Removed Hotelier(${remove_hotelier_id}) from Hotel Group(${remove_hotel_group_id})`
        );
      } else {
        console.log(
          `Failed to remove Hotelier(${remove_hotelier_id}) from Hotel Group(${remove_hotel_group_id})`
        );
      }
    } else {
      console.log(hotelierHotelGroupId);
    }
  };

  const renderHotelGroupName = (
    hotelier_id: number,
    hotel_group: HotelGroup
  ) => {
    if (hotel_group.name === undefined) {
      return (
        <Row>
          <Col>N/A</Col>
          <Col>
            <Button variant='outline-primary' size='sm'>
              <PlusCircle />
            </Button>
          </Col>
        </Row>
      );
    } else {
      return (
        <Row>
          <Col>{`${hotel_group.name}`}</Col>
          <Col>
            <Button
              variant='outline-danger'
              size='sm'
              type='button'
              onClick={() => handleRemoveHotelier(hotelier_id, hotel_group.id)}
            >
              <DashCircle />
            </Button>
          </Col>
        </Row>
      );
    }
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
            <td>
              {renderHotelGroupName(hotelier.hotelier_id, hotelier.hotel_group)}
            </td>
          </tr>
        ))}
      </tbody>
    </Table>
  );
};

export default Hoteliers;
