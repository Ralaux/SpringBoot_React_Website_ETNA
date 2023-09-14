import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';

import Deconnexion from './Deconnexion';
import Backhome from './Backhome';
import MyComment from './Mycomments';

const Parameters = () => {

    return (
        <div>
            <h1>Mon profil</h1>

            <Backhome />

            <p id='comment'>
                Username : {sessionStorage.getItem('username')} <br />
                Role : {sessionStorage.getItem('role')}
            </p>
            <div id='parambutton'>
                <Deconnexion />

                {sessionStorage.getItem('role') === 'ROLE_ADMIN' && (
                    <Link to="/userlist">
                        <button> UserList </button>
                    </Link>
                )}
            </div>
            <MyComment />

        </div>
    );
};

export default Parameters;
