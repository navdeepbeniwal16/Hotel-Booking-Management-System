import React, { useEffect, useState, useContext } from 'react';
import UserDataType from '../../types/UserDataType';
import { toString as addressToString } from '../../types/AddressType';
import AppContext from '../../context/AppContext';
import HotelGroup, {
  defaultHotelGroup,
  makeHotelGroup,
} from '../../types/HotelGroup';
import Hotel from '../../types/HotelType';
import { Button } from 'react-bootstrap';
import HotelsList from '../../components/molecules/HotelsList';

interface HotelierBody {
  hotelier: UserDataType;
}
const HotelierBody = ({ hotelier }: HotelierBody) => {
  const [group, setGroup] = useState(defaultHotelGroup);
  const noHotels: Array<Hotel> = [];
  const [hotels, setHotels] = useState(noHotels);
  const { backend } = useContext(AppContext.GlobalContext);
  useEffect(() => {
    const setup = async () => {
      backend.getHotelGroups().then((groups: HotelGroup[]) => {
        if (groups.length == 1) setGroup(groups[0]);
      });
      backend
        .getHotelsForGroup(makeHotelGroup(hotelier.group))
        .then((hotels: Hotel[]) => {
          setHotels(hotels);
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
      <div>
        <h3>Hotels</h3>
        <div className='my-2'>
          <Button
            variant='primary'
            onClick={() => console.log('create a new hotel')}
          >
            Create hotel
          </Button>
        </div>
        <HotelsList hotels={hotels} />
      </div>
    </>
  );
};

export default HotelierBody;
