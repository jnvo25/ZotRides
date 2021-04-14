
import {useState} from 'react';

import {Card, Col, Container, Row, Fade} from "react-bootstrap";
import LoadingOverlay from 'react-loading-overlay';

import SignIn from "./Login/SignIn";
import SignUp from "./Login/SignUp";

export default function() {
    const [formType, setForm] = useState('login');
    const [signUpStatus, setSignUp] = useState(false);
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

    return(
        <Container>
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
                            {
                                formType === 'login' &&
                                <SignIn switchForm={switchForm} setLoading={setLoading} />
                            }
                            {
                                formType === 'register' &&
                                <SignUp switchForm={switchForm} setLoading={setLoading} />
                            }
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