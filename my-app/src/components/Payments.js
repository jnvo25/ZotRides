import {Container, Row, Col, Modal} from "react-bootstrap";
import Header from "./Template/Header";
import PaymentForm from "./Payments/PaymentForm";
import {useEffect, useState} from "react";
import jQuery from "jquery";
import HOST from "../Host";

import CarCard from "./Home/CarCard";
import CartItem from "./MyCart/CartItem";
import ConfirmationCard from "./MyCart/ConfirmationCard";

export default function() {
    const [isLoading, setLoading] = useState(true);
    const [cars, setCars] = useState([]);
    const [completed, setCompleted] = useState(false);
    const [isFailed, setFailed] = useState(false);

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

    const handleClose = () => setFailed(false);

    console.log(cars);
    if (isLoading) {
        return (<div>Loading...</div>)
    }
    if (isFailed) {
        return (
            <Modal show={isFailed} onHide={handleClose} animation={false}>
                <Modal.Header closeButton>
                    <Modal.Title>ALERT</Modal.Title>
                </Modal.Header>
                <Modal.Body>Transaction failed, please check your payment info</Modal.Body>
            </Modal>
        )
    }
    console.log(completed);
    if (completed) {
        return (
            <div>
                <Header title={"Confirmation"}/>
                <Container>
                    <Row>
                        <Col>
                            <h1>Thank you for your order!</h1>
                        </Col>
                    </Row>
                    <Row>
                        <Col>
                            <h3>Sales ID: {12341234}</h3>
                            <h3>Total Price: ${calculateTotal(cars)}</h3>
                        </Col>
                    </Row>
                    <Row>
                        <h2>Your reservations</h2>
                    </Row>
                    <Row>
                        {cars.map((car, index) => (
                            <Col xs={4}>
                                <ConfirmationCard
                                    key={index}
                                    id={car.id}
                                    name={car.name}
                                    location={car.pickupLocation}
                                    start={car.startDate}
                                    end={car.endDate}
                                    price={car.unitPrice}
                                />
                            </Col>
                        ))}
                    </Row>
                </Container>
            </div>
        )
    }
    return (
        <div>
            <Header title={"Payment"}/>
            <Container>
                <Row>
                    <Col>
                        <h1>Cart Price: ${calculateTotal(cars)}</h1>
                    </Col>
                    <Col>
                        <PaymentForm setFailed={setFailed} setCompleted={setCompleted}/>
                    </Col>
                </Row>
            </Container>
        </div>

    )
}

function calculateTotal(cars) {
    var sum = 0;
    for(var i=0; i<cars.length; i++) {
        const element = cars[i];
        sum += parseInt(daysInBetween(element.startDate, element.endDate) * element.unitPrice);
    }
    return sum;
}

function daysInBetween(start, end) {
    const oneDay = 24 * 60 * 60 * 1000; // hours*minutes*seconds*milliseconds
    const fDate = start.split("/");
    const sDate = end.split("/");
    const firstDate = new Date(fDate[0], fDate[1], fDate[2]);
    const secondDate = new Date(sDate[0], sDate[1], sDate[2]);
    return Math.round(Math.abs((firstDate - secondDate) / oneDay));
}