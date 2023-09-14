import { Routes, Route } from 'react-router-dom'; // React router = le gestionnaire des routes de l'app
import './App.css';

// Routes :
import Home from "./components/Home";
import Login from "./components/Login";
import Register from "./components/Register";
import CharacterPage from "./components/Characterpage";
import Parameters from './components/Parameters';
import Userlist from './components/Userlist';
import UpdateUser from './components/Updateuser';

function App() {                        // Tout se passe dans le "App.js" => c'est ici qu'on ca afficher les components avec la synthaxe <component/>

  return (

    <div className="App">

      <Routes>
        <Route path="/" element={<Home />} />                   {/* Ici on dit que la route de base va être HOME, et c'est là qu'on va définir nos routes */}
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/character/:id" element={<CharacterPage />} />
        <Route path="/parameters" element={<Parameters />} />
        <Route path="/userlist" element={<Userlist />} />
        <Route path="/update-user" element={<UpdateUser />} />
      </Routes>

    </div>
  );
}

export default App;


// Ajouter un bouton de déconnection qui n'apparait que si on est connecté, pour se déconnécter => fait automatiquement revenir à l'accueil