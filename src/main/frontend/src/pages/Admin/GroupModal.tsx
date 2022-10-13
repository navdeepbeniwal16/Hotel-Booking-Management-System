import React, { useState, ChangeEvent, useContext } from 'react';
import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';
import Modal from 'react-bootstrap/Modal';
import Spinner from 'react-bootstrap/Spinner';
import HotelGroup, { defaultHotelGroup } from '../../types/HotelGroup';
import Hotelier from '../../types/HotelierType';
import { map, find } from 'lodash';
import AppContext from '../../context/AppContext';

interface GroupModalProps {
  show: boolean;
  handleClose: () => void;
  hotelier: Hotelier;
  groups: HotelGroup[];
  addHotelier: (hotelier: Hotelier, group: HotelGroup) => void;
}
function Popup({
  show,
  handleClose,
  hotelier,
  groups,
  addHotelier,
}: GroupModalProps) {
  const { backend } = useContext(AppContext.GlobalContext);
  const [selectedGroup, selectGroup] = useState(defaultHotelGroup);
  const [waiting, setWaiting] = useState(false);

  const handleSubmit = (event: React.SyntheticEvent) => {
    event.preventDefault();
    backend.addToGroup(hotelier, selectedGroup).then((success: boolean) => {
      if (success) {
        addHotelier(hotelier, selectedGroup);
      }
      setWaiting(false);
      handleClose();
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
    <>
      <Modal show={show} onHide={handleClose} centered>
        <Form onSubmit={handleSubmit}>
          <Modal.Header closeButton>
            <Modal.Title>Add hotelier to group</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <div>ID: {hotelier.id}</div>
            <div>Name: {hotelier.name}</div>
            <div>Email: {hotelier.email}</div>
            {renderSelect()}
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
              {waiting ? <Spinner animation='border' /> : 'Save Changes'}
            </Button>
          </Modal.Footer>
        </Form>
      </Modal>
    </>
  );
}

export default Popup;
