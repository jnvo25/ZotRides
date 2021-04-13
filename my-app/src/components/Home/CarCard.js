import React from 'react';
import {Card, ListGroup, Col} from 'react-bootstrap';
import {LinkContainer} from 'react-router-bootstrap';

export default function CarCard(props) {

    return (
        <Col xs={4} className={"pt-3"}>
            <Card className={'text-center'}>
                <LinkContainer to={'/cars/' + props.id}>
                    <Card.Img
                        className={'mx-auto'}
                        variant={'top'}
                        src={'https://imagesvc.meredithcorp.io/v3/mm/image?url=https%3A%2F%2Fimg1.cookinglight.timeinc.net%2Fsites%2Fdefault%2Ffiles%2Fstyles%2Fmedium_2x%2Fpublic%2Fimage%2F2018%2F07%2Fmain%2F1807w-avocado-toast-recipe.jpg%3Fitok%3D_dDi7ZQQ'}
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
                </Card.Body>
            </Card>
        </Col>
    )
}