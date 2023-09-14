import React from 'react';
import { Link } from 'react-router-dom';

const Backhome = () => {
    return (
        <div >
            <Link to="/">
                <img
                    class="backhome"
                    src={"../learnyourmain.png"}
                    alt="wesh"
                />
            </Link>
        </div>
    );
};

export default Backhome;
