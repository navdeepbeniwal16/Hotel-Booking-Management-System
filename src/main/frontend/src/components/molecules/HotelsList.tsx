import React from 'react';

import Card from 'react-bootstrap/Card';
import Col from 'react-bootstrap/Col';
import Row from 'react-bootstrap/Row';

import Hotel from '../../types/HotelType';
import { map } from 'lodash';
import { LinkContainer } from 'react-router-bootstrap';
import { Container } from 'react-bootstrap';
import AppContext from '../../context/AppContext';
import { useContext } from 'react';
interface HotelListProps {
  hotels: Array<Hotel>;
}

const HotelsList = ({ hotels }: HotelListProps) => {
  console.log(hotels);
  const context = useContext(AppContext.GlobalContext);
  return (
    <Container>
      {map(hotels, (hotel: Hotel) => {
        return (
          <Row xs={1} md={1} className='g-4 mb-4'>
            <LinkContainer
              to={`/hotel/${hotel.hotel_id}`}
              onClick={() => {
                console.log('updating hotel context:', hotel);
                context.hotel.setHotel(hotel);
              }}
            >
              <Col>
                <Card>
                  <Card.Body>
                    <Card.Title>{hotel.name}</Card.Title>
                    <Card.Subtitle className='mb-2 text-muted'>
                      {hotel.address}
                    </Card.Subtitle>
                  </Card.Body>
                </Card>
              </Col>
            </LinkContainer>
          </Row>
        );
      })}
    </Container>
  );
};
export default HotelsList;
