import react, {useEffect, useState} from 'react';
import {Jumbotron, Container, Row, Col} from 'react-bootstrap';
import './stylesheets/Template.css';
import jQuery from 'jquery';
import {LinkContainer} from "react-router-bootstrap";
import React from "react";

export default function SingleLocation(props) {
    const [isLoading, setLoading] = useState(true);
    const [location, setLocation] = useState(EMPTY_LOCATION);

    useEffect(() => {
        jQuery.ajax({
            dataType: "json",  // Setting return data type
            method: "GET",// Setting request method
            // TODO: REMOVE HTTP://LOCALHOST WHEN BUILDING
            url: "http://localhost:8080/cs122b_spring21_team_16_war/api/single-loc?id=" + props.match.params.locationId,
            // url: "api/single-loc?id=" + props.match.params.locationId,
            success: (resultData) => {
                setLocation(resultData[0]);
                setLoading(false);
                console.log(resultData)
            }
        });
    }, [])
    if(isLoading) {
        return (<div>Loading...</div>)
    }
    return (
        <div>
            <Jumbotron className={"header"}>
                <Container>
                    <h1>Single Location Page</h1>
                </Container>
            </Jumbotron>
            <Container>
                <Row>
                    <Col>
                        <p>{location.cars_ids}</p>
                        <p>{location.cars_offered}</p>
                        <p>{location.location_address}</p>
                        <p>{location.location_id}</p>
                        <p>{location.location_phone}</p>
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

const EMPTY_LOCATION = {
    cars_ids: "NULL",
    cars_offered: "NULL",
    location_address: "NULL",
    location_id: "NULL",
    location_phone: "NULL"
}