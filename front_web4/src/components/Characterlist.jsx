import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';

function Characterlist() {

    const [characters, setCharacters] = useState([]);
    const [loaded, setLoaded] = useState(false);

    useEffect(() => {
        fetch("http://localhost:8090/character", {
        })
            .then((response) => {
                if (!response.ok) {
                    console.log(console.error);
                }
                return response.json();                                 // c'est ça qu'il me manquait ! il fallait retourner un truc :')
            })
            .then(data => {
                setCharacters(data);
                setLoaded(true)
            })
            .catch(console.error);
    }, []);

    return (
        loaded ?
            <div>
                <p id="texthome"> Des guides pour les bons, et les moins bons ...</p>
                <ul>
                    {characters.map(characters => (
                        <Link to={`/character/${characters.id}`} >
                            <div id="characterlist">
                                <li key={characters.name}   >
                                    <h3 id="namehome">{characters.name} </h3>
                                    <img
                                        src={"/characterImages/" + characters.id + ".png" || "/characterImages/default.png"}
                                        alt=""
                                        class='homeimages'
                                    />
                                </li>
                            </div>
                        </Link>
                    ))}
                </ul>
            </div> : <div> Loading ... </div>
    );
};

export default Characterlist;