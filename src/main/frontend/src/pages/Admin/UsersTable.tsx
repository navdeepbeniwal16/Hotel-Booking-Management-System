import React from 'react';
import { map } from 'lodash';
import { Table } from 'react-bootstrap';

import UserDataType from '../../types/UserDataType';

interface UserTableProps {
  users: UserDataType[];
}

const UsersTable = ({ users }: UserTableProps) => {
  return (
    <Table>
      <thead>
        <tr>
          <th>ID</th>
          <th>Name</th>
          <th>Email</th>
          <th>Role</th>
        </tr>
      </thead>
      <tbody>
        {map(users, (user: UserDataType) => (
          <tr key={user.id}>
            <td>{`${user.id}`}</td>
            <td>{`${user.name}`}</td>
            <td>{`${user.email}`}</td>
            <td>{`${user.role}`}</td>
          </tr>
        ))}
      </tbody>
    </Table>
  );
};

export default UsersTable;
