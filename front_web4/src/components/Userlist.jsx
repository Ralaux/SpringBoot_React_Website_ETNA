import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';

import Deconnexion from './Deconnexion';
import Backhome from './Backhome'
import DeleteUser from './Deleteuser';


const token = sessionStorage.getItem('token')

const UserList = () => {
    const [users, setUsers] = useState([]);
    const [loaded, setLoaded] = useState(false); //load verificator pour les API vu qu'elles sont asynchrones

    const navigate = useNavigate();
    const handleUpdateUser = (userId) => {
        // Save the userId in sessionStorage
        sessionStorage.setItem('userToModify', userId);

        // Redirect to the UpdateUser component
        navigate('/update-user');
    };

    useEffect(() => {
        const fetchUsers = async () => {
            try {
                const response = await fetch('http://localhost:8090/user', {
                    method: "GET",
                    headers: {
                        "Authorization": "Bearer " + token
                    }
                });
                if (response.ok) {
                    const data = await response.json();
                    setUsers(data);
                    setLoaded(true)
                } else {
                    console.log('Error fetching users');
                }
            } catch (error) {
                console.log('Error:', error);
            }
        };
        fetchUsers();
    }, []);

    return (


        loaded ?


            <div>

                <Backhome />

                <h1>Liste des utilisateurs</h1>
                <ul>
                    {users.map((user) => (
                        <li key={user.id}>
                            <strong>Name:</strong> {user.username}, <strong>Role:</strong> {user.role}
                            <DeleteUser userId={user.id} />
                            <button onClick={() => handleUpdateUser(user.id)}>Update User</button>
                        </li>
                    ))}
                </ul>

                <Deconnexion />

            </div> : <div> Loading ... </div>
    );
};

export default UserList;
