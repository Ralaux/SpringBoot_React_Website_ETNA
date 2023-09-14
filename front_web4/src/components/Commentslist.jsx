import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';

const Commentslist = () => {
    const [comments, setComments] = useState();

    const token = sessionStorage.getItem('token')
    const charId = Number(sessionStorage.getItem('actualCharId'))
    const [loaded, setLoaded] = useState(false);

    useEffect(() => {
        fetch("http://localhost:8090/comment/" + charId, {
            method: "GET",
            headers: { "Authorization": "Bearer " + token }
        })
            .then((response) => {
                if (!response.ok) {
                    console.log(console.error);
                }
                return response.json(); // Encore le .json qui manquait
            })
            .then(data => {
                setComments(data);
                setLoaded(true)
            })
            .catch(console.error);
    }, []);


    return (


        loaded ?
            <div>
                <ul>
                    {Array.isArray(comments)
                        ? comments.map(comment => (
                            < li key={comment.id} id='comment' >

                                <p1> Commentaire de {comment.user.username} : </p1>

                                <p>{comment.body}</p>

                            </li>
                        ))

                        : <div>Pas encore de commentaires sur ce perso ¯\_(ツ)_/¯ </div>}
                </ul>
            </div > : <div> Loading ... </div>

    );

};

export default Commentslist;
