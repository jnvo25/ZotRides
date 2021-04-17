import {Container, Jumbotron} from "react-bootstrap";
import React from "react";

import '../stylesheets/Template.css';

export default function(props) {
    return (
        <Jumbotron className={"header"}>
            <Container>
                <h1>{props.title}</h1>
            </Container>
        </Jumbotron>
    )
}