import React, { useEffect, useState } from 'react';
import DeleteMyComment from './Deletemycomment.jsx'

const MyComment = () => {
    const [comments, setComments] = useState([]);
    const [loaded, setLoaded] = useState(false);

    const token = sessionStorage.getItem('token')

    useEffect(() => {
        const fetchComments = async () => {
            try {
                const response = await fetch('http://localhost:8090/comment/mine', {
                    method: "GET",
                    headers: { "Authorization": "Bearer " + token }
                });
                if (response.ok) {
                    const data = await response.json();
                    setComments(data);
                    setLoaded(true)
                } else {
                    console.log('Error fetching comments');
                }
            } catch (error) {
                console.log('Error:', error);
            }
        };

        fetchComments();
    }, []);

    return (
        loaded ?
            <div>
                <h2>Mes commentaires</h2>
                <ul>
                    {comments.map((comment) => (
                        <li key={comment.id} id="comment">

                            <p1>Commentaire posté la page de : {comment.character.name}</p1>
                            <p>{comment.body}</p>
                            <DeleteMyComment commentId={comment.id} />


                            <br />

                        </li>
                    ))}
                </ul>
            </div> : <div> Chargement des commentaires ... </div>
    );
};

export default MyComment;
