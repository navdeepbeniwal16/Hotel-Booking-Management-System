import React from 'react';

import Card from 'react-bootstrap/Card';
import Col from 'react-bootstrap/Col';
import Row from 'react-bootstrap/Row';

import Hotel from '../../types/HotelType';
import { map } from 'lodash';
import { Container, Nav } from 'react-bootstrap';
import AppContext from '../../context/AppContext';
import { useContext } from 'react';
import { Link } from 'react-router-dom';
import { toString } from '../../types/AddressType';
interface HotelListProps {
  hotels: Array<Hotel>;
}

const HotelsList = ({ hotels }: HotelListProps) => {
  const context = useContext(AppContext.GlobalContext);
  return (
    <Container>
      {map(hotels, (hotel: Hotel) => {
        return (
          <Row key={`${hotel.id}`} xs={1} md={1} className='g-4 mb-4'>
            <Nav.Link
              as={Link}
              to={`/hotel/${hotel.id}`}
              onClick={() => {
                context.hotel.setHotel(hotel);
              }}
            >
              <Col>
                <Card>
                  <Card.Body>
                    <Card.Title>{hotel.name}</Card.Title>
                    <Card.Subtitle className='mb-2 text-muted'>
                      {toString(hotel.address)}
                    </Card.Subtitle>
                  </Card.Body>
                </Card>
              </Col>
            </Nav.Link>
          </Row>
        );
      })}
    </Container>
  );
};
export default HotelsList;
