import react, {useEffect, useState} from 'react';
import {Jumbotron, Container, Row, Col} from 'react-bootstrap';
import './stylesheets/Template.css';
import jQuery from 'jquery';
import {LinkContainer} from "react-router-bootstrap";
import React from "react";
import Header from "./Template/Header";

export default function SingleCar(props) {
    const [isLoading, setLoading] = useState(true);
    const [car, setCar] = useState(EMPTY_CAR);

    useEffect(() => {
        jQuery.ajax({
            dataType: "json",
            method: "GET",
            // TODO: REMOVE HTTP://LOCALHOST WHEN BUILDING
            url: "http://localhost:8080/cs122b_spring21_team_16_war/api/single-car?id=" + props.match.params.carId,
            // url: "api/single-car?id=" + props.match.params.carId,
            success: (resultData) => {
                setCar(resultData[0]);
                setLoading(false);
            }
        });
    }, [])
    if(isLoading) {
        return (<div>Loading...</div>)
    }
    return (
        <div>
            <Header title={"Single Car Page"}/>
            <Container>
                <Row>
                    <Col>
                        <p>{car.car_id}</p>
                        <p>{car.car_name}</p>
                        <p>{car.car_category}</p>
                        <p>{car.car_rating}</p>
                        <p>{car.car_votes}</p>
                        <p>{car.location_address}</p>
                        <p>{car.location_phone}</p>
                    </Col>
                </Row>
                <Row>
                    <LinkContainer to={'/'}>
                        <a>>>Back to homepage</a>
                    </LinkContainer>
                </Row>
            </Container>
        </div>
    )
}

const EMPTY_CAR = {
    car_category: "NULL",
    car_id: "NULL",
    car_name: "NULL",
    car_rating: -1,
    car_votes: -1,
    location_address: ["NULL","NULL","NULL"],
    location_phone: ["NULL","NULL","NULL"]
}