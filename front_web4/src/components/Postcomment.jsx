import React, { useState } from 'react';
import { Link } from 'react-router-dom';

const Postcomment = () => {
    const [comment, setComment] = useState('');
    const token = sessionStorage.getItem('token')

    const handleSubmit = async (e) => {
        e.preventDefault();

        try {
            const response = await fetch(' http://localhost:8090/comment', {
                method: 'POST',
                headers: {
                    "Authorization": "Bearer " + token,
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    "body": comment,
                    "character_id": Number(sessionStorage.getItem('actualCharId'))
                }),
            });

            if (response.ok) {
                console.log('Comment posted successfully');
                setComment('');
                window.location.reload();
            } else {
                // Error occurred while posting the comment
                console.log('Error posting comment');
            }
        } catch (error) {
            console.log('Error:', error);
        }
    };

    return (

        sessionStorage.getItem('token') ?
            <div>
                <h2>Commentaires</h2>
                <form onSubmit={handleSubmit}>
                    <textarea
                        value={comment}
                        onChange={(e) => setComment(e.target.value)}
                        placeholder="Un petit commentaire sur le personnage ?"
                        rows={4}
                        cols={100}
                    />
                    <br />
                    <button id='petitbutton' type="submit">Poster</button>
                </form>
            </div> : <p>Connecte toi pour poster un commentaire bg : <Link to="/login" id='invertregisterlogin'>Me login</Link></p>

    );
};

export default Postcomment;
