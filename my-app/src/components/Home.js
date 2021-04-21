import react, {useEffect, useState} from 'react';
import {Jumbotron, Container, Row, Col} from 'react-bootstrap';
import './stylesheets/Home.css';

import CarCard from "./Home/CarCard";
import jQuery from 'jquery';
import HOST from '../Host';

export default function Home() {
    const [isLoading, setLoading] = useState(true);
    const [cars, setCars] = useState([]);

    useEffect(() => {

        jQuery.ajax({
            dataType: "json",
            method: "GET",
            url: HOST + "api/cars",
            success: (resultData) => {
                setCars(resultData);
                setLoading(false);
                console.log(resultData);
            }
        });
    }, [])

    if (isLoading) {
        return (<div>Loading...</div>)
    }
    return (
        <div>
            <Jumbotron className={"hero-section"}>
                <h1>Welcome to ZotRides</h1>
                <p className={"subtitle"}>Book unforgettable cars from locations around the world</p>
            </Jumbotron>
            <Container fluid>
                <Row>
                    {cars.map((car, index) => (
                        <CarCard
                            key={index}
                            id={car.car_id}
                            name={car.car_name}
                            category={car.car_category}
                            rating={car.car_rating}
                            votes={car.car_votes}
                            locations={car.location_address.split(';')}
                            locationId={car.location_ids.split(';')}
                            phone={car.location_phone.split(';')}
                        />
                    ))}
                </Row>
            </Container>
        </div>
    );
}
