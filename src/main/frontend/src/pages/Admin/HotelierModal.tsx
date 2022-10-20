import React, { useState, ChangeEvent, useContext } from 'react';
import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';
import Modal from 'react-bootstrap/Modal';
import Spinner from 'react-bootstrap/Spinner';
import HotelGroup, { defaultHotelGroup } from '../../types/HotelGroup';
import Hotelier from '../../types/HotelierType';
import { map, find } from 'lodash';
import AppContext from '../../context/AppContext';
import { Toast } from 'react-bootstrap';

interface HotelierModalProps {
  show: boolean;
  handleClose: () => void;
  hotelier: Hotelier;
  groups: HotelGroup[];
  addHotelier: (hotelier: Hotelier, group: HotelGroup) => void;
}
function HotelierModal({
  show,
  handleClose,
  hotelier,
  groups,
  addHotelier,
}: HotelierModalProps) {
  const { backend } = useContext(AppContext.GlobalContext);
  const [selectedGroup, selectGroup] = useState(defaultHotelGroup);
  const [waiting, setWaiting] = useState(false);
  const [error, setError] = useState('');

  const handleSubmit = (event: React.SyntheticEvent) => {
    event.preventDefault();
    backend
      .addToGroup(hotelier, selectedGroup)
      .then(([success, _error]: [boolean, string]) => {
        if (success) {
          addHotelier(hotelier, selectedGroup);
          handleClose();
        } else {
          setError(_error);
          setTimeout(() => {
            handleClose();
            window.location.reload();
          }, 2500);
        }
        setWaiting(false);
      });
    setWaiting(true);
  };

  const renderSelect = () => {
    return (
      <Form.Group className='mb-3' controlId='groupForm.Select'>
        <Form.Label>Hotel groups</Form.Label>
        <Form.Select
          onChange={(e: ChangeEvent<HTMLSelectElement>) => {
            const n: number = Number.parseInt(e.target.value);
            if (Number.isInteger(n)) {
              const group = find(groups, (group: HotelGroup) => group.id == n);
              group && selectGroup(group);
            }
          }}
          aria-label='Hotel groups dropdown'
        >
          <option>Select a hotel group</option>

          {map(groups, (group: HotelGroup) => (
            <option key={group.id} value={group.id}>
              {group.name}
            </option>
          ))}
        </Form.Select>
      </Form.Group>
    );
  };

  return (
    <Modal show={show} onHide={handleClose} centered>
      <Form onSubmit={handleSubmit}>
        <Modal.Header closeButton>
          <Modal.Title>Add hotelier to group</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          {error ? (
            <Toast bg='danger' className='text-white'>
              <Toast.Header>Error</Toast.Header>
              <Toast.Body>Somthing went wrong: {error}</Toast.Body>
            </Toast>
          ) : (
            <>
              <div>ID: {hotelier.id}</div>
              <div>Name: {hotelier.name}</div>
              <div>Email: {hotelier.email}</div>
              {renderSelect()}
            </>
          )}
        </Modal.Body>
        <Modal.Footer>
          <Button disabled={waiting} variant='secondary' onClick={handleClose}>
            Close
          </Button>
          <Button disabled={waiting} type='submit' variant='primary'>
            {waiting ? <Spinner animation='border' /> : 'Save Changes'}
          </Button>
        </Modal.Footer>
      </Form>
    </Modal>
  );
}

export default HotelierModal;
