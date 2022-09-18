import React from 'react';
import Card from 'react-bootstrap/Card';
import Col from 'react-bootstrap/Col';

const ListingCard = () => {
  return (
    <Col>
      <Card>
        <Card.Body>
          <Card.Title>Hotel Title</Card.Title>
          <Card.Subtitle className='mb-2 text-muted'>Location</Card.Subtitle>
          <Card.Text>Maybe Some Description</Card.Text>
          <Card.Link href='#'>Details</Card.Link>
          <Card.Link href='#'>Link 2</Card.Link>
        </Card.Body>
      </Card>
    </Col>
  );
};

export default ListingCard;
