import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import Backhome from './Backhome';
import Postcomment from './Postcomment';
import LoginOrRegisterButton from './LoginOrRegisterButton';
import ParametersButton from './Parametersbutton';
import Commentslist from './Commentslist';


const CharacterPage = () => {
    const { id } = useParams(); // id utilisé pour arriver à la page
    const [character, setCharacter] = useState({}); // hook pr API
    const [loaded, setLoaded] = useState(false); //load verificator pour les API vu qu'elles sont asynchrones

    useEffect(() => {
        fetch("http://localhost:8090/character/" + id, {
        })
            .then((response) => {
                if (!response.ok) {
                    console.log(console.error);
                }
                return response.json();                                 // c'est ça qu'il me manquait ! il fallait retourner un truc :')
            })
            .then(data => {
                setCharacter(data);
                sessionStorage.setItem('actualCharId', id)
                setLoaded(true)
            })
            .catch(console.error);
    }, []);

    return (

        loaded ?

            <div>
                <Backhome />
                <LoginOrRegisterButton />
                <h1>{character.name}</h1>

                <img
                    src={"/characterImages/" + id + ".png" || "/characterImages/default.png"}
                    alt=""
                    style={{ width: '200px', height: '200px' }}
                />

                <div id="characterDesc">
                    <div id="characterStats">
                        <h2> Stats </h2>

                        <p>
                            Speed : {character.speed} <br />
                            Wheight : {character.weight}
                        </p>

                        <h2> Attaques </h2>

                        <h3>
                            up_tilt
                        </h3>

                        Dégats : {character.up_tilt.damage} <br />
                        Nombre de frames : {character.up_tilt.frame}<br />
                        Endlag : {character.up_tilt.endlag}<br />

                        <h3>
                            forward_tilt
                        </h3>

                        Dégats : {character.forward_tilt.damage} <br />
                        Nombre de frames : {character.forward_tilt.frame}<br />
                        Endlag : {character.forward_tilt.endlag}<br />

                        <h3>
                            down_tilt
                        </h3>

                        Dégats : {character.down_tilt.damage} <br />
                        Nombre de frames : {character.down_tilt.frame}<br />
                        Endlag : {character.down_tilt.endlag}<br />

                        <h3>
                            dash_attack
                        </h3>

                        Dégats : {character.dash_attack.damage} <br />
                        Nombre de frames : {character.dash_attack.frame}<br />
                        Endlag : {character.dash_attack.endlag}<br />

                    </div>
                </div>
                <h2> Guide </h2>

                <div>
                    <h3>Vidéo guide pour {character.name} de {character.video.yt_channel}</h3>
                    <iframe
                        title='titre'
                        src={character.video.url.replace("watch?v=", "embed/") // on parse la video et on change le watch par embed snn youtube laisse pas
                        }
                        allowFullScreen
                    ></iframe>
                </div>

                <Postcomment />
                <ParametersButton />


                <Commentslist />

            </div> : <div> Loading ... </div>

    );

};

export default CharacterPage;
