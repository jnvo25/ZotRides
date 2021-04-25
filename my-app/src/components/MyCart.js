import Header from "./Template/Header";
import CartItem from "./MyCart/CartItem";
import {Container, Button, Row, Col, Image} from "react-bootstrap";
import UpdateCartForm from './MyCart/UpdateCartForm';
import SearchForm from "./Search/SearchModal/SearchForm";
import {LinkContainer} from 'react-router-bootstrap';
import React, {useState, useEffect} from "react";
import jQuery from "jquery";
import HOST from "../Host";
import CarCard from "./Home/CarCard";

export default function() {
    const [isLoading, setLoading] = useState(true);
    const [cars, setCars] = useState([]);

    useEffect(() => {
        jQuery.ajax({
            dataType: "json",
            method: "GET",
            url: HOST + "api/shopping-cart",
            success: (resultData) => {
                setCars(resultData.previousItems);
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
        <Header title={"My Cart"}/>
        <Container>
            <Row>
                <Col xs={8}>
                    {cars.map((car, index) => (
                        <CartItem
                            key={index}
                            id={car.id}
                            name={car.name}
                            location={car.pickupLocation}
                            start={car.startDate}
                            end={car.endDate}
                            price={car.unitPrice}
                        />
                    ))}
                </Col>
                <Col>
                    <LinkContainer to={"/payment"}>
                        <Button>Continue to Payment</Button>
                    </LinkContainer>

                </Col>
            </Row>
            <Row>
                <LinkContainer to={'/browse/na/na/na/na/na/t'}>
                    <Button variant="link">
                        >> Back to Car List
                    </Button>
                </LinkContainer>
            </Row>
        </Container>
        </div>

    )
}