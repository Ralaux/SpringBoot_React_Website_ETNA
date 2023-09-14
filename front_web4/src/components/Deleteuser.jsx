import React from 'react';
import { useNavigate } from 'react-router-dom';

const DeleteUser = ({ userId }) => {

    const token = sessionStorage.getItem('token')
    const navigate = useNavigate();


    const handleDelete = async () => {
        try {
            const response = await fetch(` http://localhost:8090/user/${userId}`, { // je ne sais pas pk il considre pas ça comme un integer alors que pour le delete des comment ça fonctionne
                method: 'DELETE',
                headers: { "Authorization": "Bearer " + token }
            });

            if (response.ok) {
                if (sessionStorage.getItem('myId') != userId) {
                    console.log('User deleted successfully');
                    window.location.reload();
                } else {
                    console.log('Autodestruction Successfull');
                    sessionStorage.clear();
                    navigate("/");
                }

            } else {
                console.log('Error deleting user');
            }
        } catch (error) {
            console.log('Error:', error);
        }
    };

    return (
        <button onClick={handleDelete}>Supprimer l'utilisateur</button>
    );
};

export default DeleteUser;
