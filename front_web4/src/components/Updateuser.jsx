import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';

const token = sessionStorage.getItem('token');

const Updateuser = () => {
    const [newUsername, setNewUsername] = useState('');
    const [selectedRole, setSelectedRole] = useState('');
    const navigate = useNavigate();

    const handleUsernameChange = (event) => {
        setNewUsername(event.target.value);
    };

    const handleRoleChange = (event) => {
        setSelectedRole(event.target.value);
    };

    const userModif = sessionStorage.getItem('userToModify')

    const handleUpdateUser = async () => {
        try {
            const response = await fetch('http://localhost:8090/user/' + userModif, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    "Authorization": "Bearer " + token
                },
                body: JSON.stringify({ username: newUsername, role: selectedRole }),
            });

            if (response.ok) {
                console.log('User updated successfully');
                sessionStorage.removeItem('userToModify');
                navigate('/user-list');
                window.location.reload();
                // Handle any additional actions or notifications
            } else {
                console.log('Error updating user');
                // Handle error case
            }
        } catch (error) {
            console.log('Error:', error);
            // Handle error case
        }
    };

    return (
        <div>

            <h1>Update le user {userModif}</h1>

            <label htmlFor="username">Changer le Username:</label>
            <input
                type="text"
                id="username"
                value={newUsername}
                onChange={handleUsernameChange}
            />

            <label>
                <input
                    type="radio"
                    name="role"
                    value="ROLE_ADMIN"
                    checked={selectedRole === 'ROLE_ADMIN'}
                    onChange={handleRoleChange}
                />
                Admin ?
            </label>
            <label>
                <input
                    type="radio"
                    name="role"
                    value="ROLE_USER"
                    checked={selectedRole === 'ROLE_USER'}
                    onChange={handleRoleChange}
                />
                User ?
            </label>

            <button onClick={handleUpdateUser}>Update le user</button>
        </div>
    );
};

export default Updateuser;
