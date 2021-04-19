import Header from "./Template/Header";
import {useEffect, useState} from "react";
import jQuery from "jquery";
import {Container, Row, Col} from "react-bootstrap";
import CarCard from "./Home/CarCard";

export default function(props) {
    const [isLoading, setLoading] = useState(true);
    const [cars, setCars] = useState([]);

    useEffect(() => {
        jQuery.ajax({
            dataType: "json",
            method: "GET",
            data: removeEmpty(props.match.params),
            url: "http://localhost:8080/cs122b_spring21_team_16_war/api/search-car",
            success: (resultData) => {
                if (resultData.status === "fail")
                    props.setError("Login failed (Invalid username/password");
                else
                    setCars(resultData);
                setLoading(false);
            }
            // setComplete(true);
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
                    <Col>
                        <p>Model: {props.match.params.model}</p>
                        <p>Year: {props.match.params.year}</p>
                        <p>Make: {props.match.params.make}</p>
                        <p>Location: {props.match.params.location}</p>
                    </Col>
                </Row>
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

function removeEmpty(object) {
    if(object.model === "na" || object.model == null)
        delete object.model;
    if(object.year === "na" || object.year == null)
        delete object.year;
    if(object.make === "na" || object.make == null)
        delete object.make;
    if(object.location === "na" || object.location == null)
        delete object.location;
    return object;
}