import React, { useState } from 'react';
import { Modal, Button, Form } from 'react-bootstrap';
import Hotel, { defaultHotel } from '../../types/HotelType';

interface CreateHotelProps {
  show: boolean;
  onSubmit: (hotel: Hotel) => void;
  onClose: () => void;
}

const CreateHotel = ({ show, onSubmit, onClose }: CreateHotelProps) => {
  const [hotel, setHotel] = useState(defaultHotel);
  const [valid, setValid] = useState(false);
  const handleOnEnter = () => {
    console.log('onEnter');
  };

  const handleOnExit = () => {
    console.log('onExit');
  };

  const handleSubmit = (event: React.SyntheticEvent) => {
    event.preventDefault();
    if (valid) {
      console.log('submitting');
      onSubmit(hotel);
    } else {
      console.log('invalid - can\'t submit');
    }
  };

  return (
    <Modal
      show={show}
      onHide={onClose}
      backdrop='static'
      scrollable={true}
      onEnter={handleOnEnter}
      onExit={handleOnExit}
    >
      <Form onSubmit={handleSubmit}>
        <Modal.Header>
          <Modal.Title>Create a new hotel</Modal.Title>
        </Modal.Header>
        <Modal.Body>Lorem ipsum</Modal.Body>
        <Modal.Footer>
          <Button variant='secondary' onClick={onClose}>
            Close
          </Button>
          <Button type='submit' variant='primary' >
            Create
          </Button>
        </Modal.Footer>
      </Form>
    </Modal>
  );
};

export default CreateHotel;
