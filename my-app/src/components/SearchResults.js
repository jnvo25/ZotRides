import Header from "./Template/Header";
import {useEffect, useState} from "react";
import jQuery from "jquery";
import {Container, Jumbotron, Row} from "react-bootstrap";
import CarCard from "./Home/CarCard";

export default function(props) {
    const [isLoading, setLoading] = useState(true);
    const [cars, setCars] = useState([]);

    useEffect(() => {
        console.log("USER QUERY: " + props.userQuery)
        console.log(props.userQuery)
        jQuery.ajax({
            dataType: "json",
            method: "GET",
            // TODO: REMOVE HTTP://LOCALHOST WHEN BUILDING
            url: "http://localhost:8080/cs122b_spring21_team_16_war/api/cars",
            // url: "api/cars",
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
            <Header title={"Search Results"}/>
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