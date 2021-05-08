import {Card, Col, Container, Fade, Nav, Row} from "react-bootstrap";
import "./stylesheets/EmployeeLogin.css";
import Dashboard from "./EmployeeLogin/Dashboard";
import AnchorLink from 'react-anchor-link-smooth-scroll';
import {useState, useEffect} from "react";
import SignIn from "./EmployeeLogin/Dashboard/SignIn";
import LoadingOverlay from "react-loading-overlay";
import jQuery from "jquery";
import HOST from "../Host";

export default function () {
    const [success, setSuccess] = useState(false);
    const [isLoading, setLoading] = useState(false);
    const [error, setError] = useState();

    if(success) {
        return (
            <Container>
                <Row>
                    <Col>
                    <h1 className={"text-center pt-5"}>Login</h1>
                    <Card xs={4}>
                        <LoadingOverlay active={isLoading} spinner text={'Logging in...'}>
                            <Card.Body>
                                <SignIn setLoading={setLoading} setError={setError} setSuccess={setSuccess} />
                            </Card.Body>
                            <div>{error}</div>
                        </LoadingOverlay>
                    </Card>
                    </Col>
                </Row>
            </Container>
        )
    }
    return (
        <Container fluid>
            <Row>
                <Col xs={2} className={"sidebar"}>
                    <Nav defaultActiveKey={"tables"} className={"flex-column"}>
                        <AnchorLink href='#tables'><Nav.Link className={"nav-link"} eventKey={"tables"}>Tables</Nav.Link></AnchorLink>
                        <AnchorLink href='#addcar'><Nav.Link className={"nav-link"} eventKey={"addcar"}>Add Car</Nav.Link></AnchorLink>
                        <AnchorLink href='#addpickup'><Nav.Link className={"nav-link"} eventKey={"addpickup"}>Add Pickup Location</Nav.Link></AnchorLink>
                    </Nav>
                </Col>
                <Col xs={10}>
                    <Dashboard />
                </Col>
            </Row>
        </Container>
    )
}