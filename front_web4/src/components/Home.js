import React from 'react';
import LoginOrRegisterButton from './LoginOrRegisterButton';
import Characterlist from './Characterlist';
import ParametersButton from './Parametersbutton';

function Home() {
  return (
    <div>
      <LoginOrRegisterButton />

      <img
        src={"./learnyourmain.png"}
        alt=""
        id='learnyourmainhome'
      />

      <Characterlist />
      <ParametersButton />
    </div>
  );
}

export default Home;