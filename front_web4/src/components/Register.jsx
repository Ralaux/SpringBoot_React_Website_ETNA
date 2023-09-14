import { Link, useNavigate } from 'react-router-dom';
import React, { useState } from "react";          //sate = component mémoire pour changer des éléments sans recharge de page
import Backhome from './Backhome';

// d'abord créer le component en lui même :

export const Register = (props) => {                     // on le créé sous forme de fonction visiblement
    const [username, setUsername] = useState('');               // stocker le username
    const [password, setPassword] = useState('');               // stocker le password
    // il faudra rajouter la meme pour le mail quand il sera ajouté + l'ajouter dans le return() plus bas.
    const navigate = useNavigate(); // Pour passer à la page login en cas de réussite

    const handleSubmit = (e) => {                                // utiliser les données qui sont entrées par les users = au début juste un console.log
        e.preventDefault();                                      // c'est le "e" entre parenthèses => il sert a prevent d'une erreur sans recharger la page et lose nos données ?

        // Requete de récupération de token/connection

        fetch("http://localhost:8090/register", {
            method: "POST",
            headers: {
                'Content-Type': 'application/json' // meme si il est déjà dit dans spring, il faut quand meme remettre le content-type ici ... alors que dans postman non bizarre
            },
            body: JSON.stringify({
                "username": username,
                "password": password
            })
        }).then((response) => {
            if (response.ok) {
                console.log('Register réussi !');
                navigate('/login'); // + message qui dit qu'on est bien registered
            }
            return response.json(); // c'est ça qu'il me manquait ! il fallait retourner un truc :')
        })
            .then(data => console.log(data))
            .catch(error => console.error(error));
    };

    return (
        <div>
            <Backhome />
            <h1>Register</h1>
            <form onSubmit={handleSubmit} id='form'>
                <label>
                    Username:
                    <input value={username} id="username" onChange={(e) => setUsername(e.target.value)} type="text" name="username" />
                </label>
                <label>
                    Password:
                    <input value={password} id="password" onChange={(e) => setPassword(e.target.value)} type="password" name="password" />
                </label> <br /><br /><br />
                <button id='petitbutton' type="submit">Register</button>
            </form>
            <p>Déjà un compte ? <Link to="/login" id='invertregisterlogin'>Me login</Link></p>
        </div>
    );

}

export default Register;