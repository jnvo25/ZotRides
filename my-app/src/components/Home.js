import {Jumbotron} from 'react-bootstrap';
import './stylesheets/Home.css';

export default function Home() {
    return (
        <div>
            <Jumbotron className={"hero-section"}>
                <h1>Welcome to ZotRides</h1>
                <p className={"subtitle"}>Book unforgettable cars from locations around the world</p>
            </Jumbotron>
        </div>
    );
}
