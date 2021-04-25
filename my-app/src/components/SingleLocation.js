import react, {useEffect, useState} from 'react';
import {Jumbotron, Container, Row, Col, Button} from 'react-bootstrap';
import './stylesheets/Template.css';
import jQuery from 'jquery';
import {LinkContainer} from "react-router-bootstrap";
import React from "react";
import Header from "./Template/Header";
import HOST from "../Host";

export default function SingleLocation(props) {
    const [isLoading, setLoading] = useState(true);
    const [location, setLocation] = useState(EMPTY_LOCATION);

    useEffect(() => {
        jQuery.ajax({
            dataType: "json",  // Setting return data type
            method: "GET",// Setting request method
            url: HOST + "api/single-loc?id=" + props.match.params.locationId,
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
    console.log(location)
    return (
        <div>
            <Header title={"Single Location Page"} />
            <Container fluid>
                <Row>
                    <Col xs={7}>
                        <Row>
                            <Col xs={2}>
                                <p style={{textAlign: 'right'}}>The location</p>
                            </Col>
                            <Col>
                                <h1>{location.location_address}</h1>
                                <p>{location.location_phone}</p>
                            </Col>
                        </Row>
                        <Row>
                            <Col xs={2}>
                                <p style={{textAlign: 'right'}}>Cars here</p>
                            </Col>
                            <Col>
                                {
                                    location.cars_offered.split(';').map((car, index) => (
                                        <Col>
                                            <LinkContainer to={"/cars/" + location.cars_ids.split(";")[index]}>
                                                <Button variant="link" key={index}>{car}</Button>
                                            </LinkContainer>
                                        </Col>
                                    ))
                                }
                            </Col>
                        </Row>
                    </Col>
                </Row>
                <Row>
                    <LinkContainer to={'/browse/na/na/na/na/na/t'}>
                        <Button variant="link">
                            >> Back to Browse
                        </Button>
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