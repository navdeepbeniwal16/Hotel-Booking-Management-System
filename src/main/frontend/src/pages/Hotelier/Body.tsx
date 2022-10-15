import React, { useEffect, useState, useContext } from 'react';
import UserDataType from '../../types/UserDataType';
import { toString as addressToString } from '../../types/AddressType';
import AppContext from '../../context/AppContext';
import HotelGroup, { defaultHotelGroup } from '../../types/HotelGroup';
import { map } from 'lodash';

interface HotelierBody {
  hotelier: UserDataType;
}
const HotelierBody = ({ hotelier }: HotelierBody) => {
  const [group, setGroup] = useState(defaultHotelGroup);
  const { backend } = useContext(AppContext.GlobalContext);
  useEffect(() => {
    const setup = async () => {
      backend.getHotelGroups().then((groups: HotelGroup[]) => {
        if (groups.length == 1) setGroup(groups[0]);
      });
    };
    setup();
  }, [hotelier]);
  return (
    <>
      <div>
        <h3>Group details</h3>
        <div>Name: {group.name}</div>
        <div>Phone: {group.phone}</div>
        <div>Address: {`${addressToString(group.address)}`}</div>
      </div>
    </>
  );
};

export default HotelierBody;
