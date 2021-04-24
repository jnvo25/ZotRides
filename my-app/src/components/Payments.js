import {Container, Row, Col} from "react-bootstrap";
import Header from "./Template/Header";
import PaymentForm from "./Payments/PaymentForm";
import {useEffect, useState} from "react";
import jQuery from "jquery";
import HOST from "../Host";

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
    console.log(cars);
    if (isLoading) {
        return (<div>Loading...</div>)
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
                        <PaymentForm />
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