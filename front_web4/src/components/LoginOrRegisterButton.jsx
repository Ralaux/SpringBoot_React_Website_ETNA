import React from 'react';
import { useNavigate } from 'react-router-dom';
import Deconnexion from './Deconnexion';

function LoginOrRegisterButton() {

    const navigate = useNavigate();
    const handleButtonClick = (page) => {
        navigate(`/${page}`);
    };

    if (sessionStorage.getItem('token')) {
        return (
            <div class='loginOrRegisterButton'>
                Connecté en tant que {sessionStorage.getItem('username')} ! <br />
                <Deconnexion />
            </div>
        );
    }

    else {
        return (
            <div class='loginOrRegisterButton'>
                <button onClick={() => handleButtonClick('login')}>Login</button>
                <button onClick={() => handleButtonClick('register')}>Register</button>
            </div>
        );
    }
}

export default LoginOrRegisterButton;