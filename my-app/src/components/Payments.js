import {Container, Row, Col} from "react-bootstrap";
import Header from "./Template/Header";
import PaymentForm from "./Payments/PaymentForm";

export default function() {
    return (
        <div>
            <Header title={"Payment"}/>
            <Container>
                <Row>
                    <Col>
                        <h1>Cart Price: ${1000}</h1>
                    </Col>
                    <Col>
                        <PaymentForm />
                    </Col>
                </Row>
            </Container>
        </div>

    )
}