import React from 'react';

const DeleteMyComment = ({ commentId }) => {

    const token = sessionStorage.getItem('token')


    const handleDelete = async () => {
        try {
            const response = await fetch(` http://localhost:8090/comment/${commentId}`, {
                method: 'DELETE',
                headers: { "Authorization": "Bearer " + token }
            });

            if (response.ok) {
                console.log('Comment deleted successfully');
                window.location.reload();
            } else {
                console.log('Error deleting comment');
            }
        } catch (error) {
            console.log('Error:', error);
        }
    };

    return (
        <button onClick={handleDelete}>Supprimer le commentaire</button>
    );
};

export default DeleteMyComment;
