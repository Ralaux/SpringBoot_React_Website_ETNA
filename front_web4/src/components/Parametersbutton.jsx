import React from 'react';
import { Link } from 'react-router-dom';

const ParametersButton = () => {

    return (
        <div>
            {sessionStorage.getItem('token') && (
                <Link id="parameterButton" to="/parameters">
                    <img src="./assets/adjust.png" alt="PARAMETERS" />
                </Link>
            )}
        </div>
    );
};

export default ParametersButton;
