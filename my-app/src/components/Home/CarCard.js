import React from 'react';
import {Card, ListGroup, Col, Button} from 'react-bootstrap';
import {LinkContainer} from 'react-router-bootstrap';

export default function CarCard(props) {

    return (
        <Col xs={4} className={"pt-3"}>
            <Card className={'text-center'}>
                <LinkContainer to={'/cars/' + props.id}>
                    <Card.Img
                        className={'mx-auto'}
                        variant={'top'}
                        src={'https://d1zgdcrdir5wgt.cloudfront.net/media/vehicle/images/-GWIN1aUTRO8HI2WwVEzIA.1440x700.jpg'}
                    />
                </LinkContainer>
                <Card.Body className={'pt-1'}>
                    <LinkContainer to={'/cars/' + props.id}>
                        <Card.Title style={{fontSize: '30px'}} className={'mb-1'}>
                            {props.name}
                        </Card.Title>
                    </LinkContainer>
                    <p>{props.category}</p>
                    <p>{props.rating} &#9733; ({props.votes} votes)</p>
                    <ListGroup variant="flush">
                        {props.locations.map((location, index) => (
                            <LinkContainer to={'/locations/' + props.locationId[index]}>
                                <ListGroup.Item key={index}>
                                    {location} <br/>
                                    {props.phone[index]}
                                </ListGroup.Item>
                            </LinkContainer>
                        ))}
                    </ListGroup>
                    <LinkContainer to={'/cars/' + props.id}>
                        <Button>Reserve</Button>
                    </LinkContainer>

                </Card.Body>
            </Card>
        </Col>
    )
}