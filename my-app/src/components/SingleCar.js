import react, {useEffect, useState} from 'react';
import {Jumbotron, Container, Row, Col, Button, Dropdown} from 'react-bootstrap';
import './stylesheets/Template.css';
import jQuery from 'jquery';
import {LinkContainer} from "react-router-bootstrap";
import React from "react";
import Header from "./Template/Header";
import ReservationForm from "./SingleCar/ReservationForm";
import HOST from "../Host";
import {Redirect} from "react-router";
import {Link} from "react-router-dom";


export default function SingleCar(props) {
    const [isLoading, setLoading] = useState(true);
    const [car, setCar] = useState(EMPTY_CAR);
    const [redirect, setRedirect] = useState(false);


    useEffect(() => {
        jQuery.ajax({
            dataType: "json",
            method: "GET",
            url: HOST + "api/single-car?id=" + props.match.params.carId,
            success: (resultData) => {
                setCar(resultData[0]);
                setLoading(false);
            }
        });
    }, [])

    function handleClick() {
        setRedirect(true);
    }

    if(isLoading) {
        return (<div>Loading...</div>)
    }
    console.log(redirect);
    if(redirect) return <Redirect to={"/browse/na/na/na/na/na/t"} />
    console.log();
    return (
        <div>
            <Header title={"Single Car Page"}/>
            <Container fluid className={"pl-5"}>
                <Row>
                    <Col xs={7}>
                        <Row>
                            <Col xs={2}>
                                <p style={{textAlign: 'right'}}>The car</p>
                            </Col>
                            <Col>
                                <h1>{car.car_name}</h1>
                                <p>{car.car_rating} &#9733; ({car.car_votes} votes)</p>
                            </Col>
                        </Row>
                        <Row>
                            <Col xs={2}>
                                <p style={{textAlign: 'right'}}>Hosted at</p>
                            </Col>
                            <Col>
                                {
                                    car.location_address.split(';').map((location, index) => (
                                        <Col>
                                        <LinkContainer to={"/locations/" + car.location_ids.split(';')[index]}>
                                            <Button variant="link" key={index}>{location}</Button>
                                        </LinkContainer>
                                        </Col>

                                ))}
                            </Col>
                        </Row>
                    </Col>
                    <Col>
                        <ReservationForm name={car.car_name} carID={car.car_id} unitPrice={120} locationids={car.location_ids.split(';')} locations={car.location_address.split(';')}/>
                    </Col>
                </Row>

                <Row>
                    <Button variant="link" onClick={handleClick}>
                        >> Back to Car List
                    </Button>

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