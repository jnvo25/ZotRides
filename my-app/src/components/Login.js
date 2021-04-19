
import {useState} from 'react';

import {Card, Col, Container, Row, Fade} from "react-bootstrap";
import LoadingOverlay from 'react-loading-overlay';

import SignIn from "./Login/SignIn";
import SignUp from "./Login/SignUp";
import {Redirect} from "react-router";

export default function() {
    const [formType, setForm] = useState('login');
    const [success, setSuccess] = useState(false);
    const [error, setError] = useState();
    const [isLoading, setLoading] = useState(false);

    const switchForm = (event) => {
        setError();
        if(event.target.value === "login") {
            setForm('login');
        } else {
            setForm('register');
        }
    }

    const getError = (error) => {
        let err = null;
        error.message ? err = error.message : err = error;
        return err;
    }

    if(success)
        return (<Redirect to={"/"} />)

    return(
        <Container className={"pt-5"}>
            <Row>
                <Col>
                    <h1>Welcome to ZotRides</h1>
                </Col>
            </Row>
            <Row>
                <Col>
                    <Card>
                        <LoadingOverlay active={isLoading} spinner text={'Logging in...'}>
                        <Card.Body>
                            <SignIn switchForm={switchForm} setLoading={setLoading} setError={setError} setSuccess={setSuccess}/>
                            {/*{*/}
                            {/*    formType === 'register' &&*/}
                            {/*    <SignUp switchForm={switchForm} setLoading={setLoading} />*/}
                            {/*}*/}
                            {
                                error &&
                                <Fade in={true}>
                                    <div>
                                        <br />
                                        <p>ERROR: {getError(error)}</p>
                                    </div>
                                </Fade>
                            }
                        </Card.Body>
                        </LoadingOverlay>
                    </Card>
                </Col>
            </Row>
        </Container>
    )
}