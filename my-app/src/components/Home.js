import react from 'react';
import './stylesheets/Home.css';

import {Jumbotron} from "react-bootstrap";
import CarCard from "./Home/CarCard";

export default function Home() {
    return (
        <div>
            <Jumbotron className={"hero-section"}>
                <h1>Welcome to ZotRides</h1>
                <p className={"subtitle"}>Book unforgettable cars from locations around the world</p>
            </Jumbotron>
            <CarCard
                name={"Murcielago"}
                category={"coupe"}
                rating={"4.5"}
                votes={"17125"}
                locations={["location1", "location2", "location3"]}
                phone={["999-888-7777", "999-888-7777", "999-888-7777"]}
            />
        </div>
    );
}
