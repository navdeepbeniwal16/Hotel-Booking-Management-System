import React, { useState, useContext, ReactNode } from 'react';
import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';
import Modal from 'react-bootstrap/Modal';
import Spinner from 'react-bootstrap/Spinner';
import HotelGroup, { defaultHotelGroup } from '../../types/HotelGroup';
import { map, find } from 'lodash';
import AppContext from '../../context/AppContext';
import { ModalHeader } from 'react-bootstrap';

interface GroupModalProps {
  show: boolean;
  handleClose: () => void;
  onCreateGroup: (group: HotelGroup) => void;
}
function GroupModal({ show, handleClose, onCreateGroup }: GroupModalProps) {
  const { backend } = useContext(AppContext.GlobalContext);
  const [group, setGroup] = useState(defaultHotelGroup);
  const [waiting, setWaiting] = useState(false);
  const [phone, setPhone] = useState('');

  const handleSubmit = (event: React.SyntheticEvent) => {
    event.preventDefault();
    backend.createGroup(group).then((success: boolean) => {
      if (success) {
        onCreateGroup(group);
      }
      setWaiting(false);
      handleClose();
    });
    setWaiting(true);
  };

  const onPhoneChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    event.preventDefault();
    const { value } = event.target;
    const phoneRegex = /^\+?\d{0,11}$/g;
    const update = `${value}`;
    const isValid = phoneRegex.test(update);
    if (isValid) {
      setPhone(update);
    }
  };

  const renderNameField = (): ReactNode => {
    return (
      <Form.Group controlId='groupName'>
        <Form.Label>Name</Form.Label>
        <Form.Control
          required
          type='text'
          placeholder='Enter name'
          aria-placeholder='hotel name'
        />
        <Form.Text className='text-muted'>Name of hotel group</Form.Text>
      </Form.Group>
    );
  };

  const renderPhoneField = (): ReactNode => {
    return (
      <Form.Group controlId='groupName'>
        <Form.Label>Phone</Form.Label>
        <Form.Control
          onChange={onPhoneChange}
          required
          type='text'
          placeholder='+6103789456'
          aria-placeholder='+6103789456'
          value={phone}
        />
        <Form.Text className='text-muted'>
          Contact phone number for hotel
        </Form.Text>
      </Form.Group>
    );
  };

  return (
    <>
      <Modal show={show} onHide={handleClose} centered>
        <Form onSubmit={handleSubmit}>
          <Modal.Header closeButton>
            <Modal.Title>Create a group</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            {renderNameField()}
            {renderPhoneField()}
            <Modal.Dialog>
              <Modal.Header className='p-3'>Address</Modal.Header>
              <Form.Group className='p-3' controlId='groupName'>
                <Form.Label>Line 1</Form.Label>
                <Form.Control
                  required
                  type='text'
                  placeholder='Enter name'
                  aria-placeholder='hotel name'
                />
                <Form.Text className='text-muted'>
                  Name of hotel group
                </Form.Text>
              </Form.Group>
            </Modal.Dialog>
          </Modal.Body>
          <Modal.Footer>
            <Button
              disabled={waiting}
              variant='secondary'
              onClick={handleClose}
            >
              Close
            </Button>
            <Button disabled={waiting} type='submit' variant='primary'>
              {waiting ? <Spinner animation='border' /> : 'Create'}
            </Button>
          </Modal.Footer>
        </Form>
      </Modal>
    </>
  );
}

export default GroupModal;
