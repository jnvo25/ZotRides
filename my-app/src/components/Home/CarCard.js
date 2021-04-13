import React, {useEffect} from 'react';
import {Card, ListGroup} from 'react-bootstrap';

export default function CarCard(props) {

    useEffect(() => {

    }, [])

    return (
        <Card className={'text-center'}>
            <Card.Img
                className={'mx--auto'}
                variant={'top'}
                src={'https://imagesvc.meredithcorp.io/v3/mm/image?url=https%3A%2F%2Fimg1.cookinglight.timeinc.net%2Fsites%2Fdefault%2Ffiles%2Fstyles%2Fmedium_2x%2Fpublic%2Fimage%2F2018%2F07%2Fmain%2F1807w-avocado-toast-recipe.jpg%3Fitok%3D_dDi7ZQQ'}
            />
            <Card.Body className={'pt-1'}>
                <Card.Title style={{fontSize: '16px'}} className={'mb-1'}>
                    {props.name}
                </Card.Title>
                <Card.Text style={{fontSize: '11px'}}>
                    <p>{props.category}</p>
                    <p>{props.rating} &#9733; ({props.votes} votes)</p>
                    <p>Locations</p>
                    <ListGroup variant={"flush"}>
                        {props.locations.map((location, index) => (
                            <ListGroup.Item key={index}>
                                {location} <br/>
                                {props.phone[index]}
                            </ListGroup.Item>
                        ))}
                    </ListGroup>

                </Card.Text>
            </Card.Body>
        </Card>
    )
}