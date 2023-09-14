import React, { useState } from "react";
import { useParams } from "react-router-dom";     // Pour travailler avec des morceaux d'urls, ici en particulier c'est l'id
import { Register } from "./Register";

export default function User() {

    const [userList, setUserList] = useState();

    fetch("http://localhost:8090/user/" + useParams().id, {
        headers: { "Authorization": "Bearer " + sessionStorage.getItem('token') }
    })
        .then(res => res.json())
        .then(data => {
            setUserList(data)
        });

    return (
        <div className="Userlist">
            {JSON.stringify(userList)}
        </div>
    )
}

// Lui dire de ne prendre que des nombres