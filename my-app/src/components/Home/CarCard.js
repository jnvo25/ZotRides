import {useEffect, useState} from 'react';
import {Card, ListGroup, Col, Button} from 'react-bootstrap';
import {LinkContainer} from 'react-router-bootstrap';
import jQuery from "jquery";
import HOST from "../../Host";
import "../stylesheets/CarCard.css";

export default function CarCard(props) {
    const [imageurl, setImageurl] = useState('https://d1zgdcrdir5wgt.cloudfront.net/media/vehicle/images/-GWIN1aUTRO8HI2WwVEzIA.1440x700.jpg')
    useEffect(() => {
        jQuery.ajax({
            dataType: "json",
            method: "GET",
            data: {make: jankyCarMake(props.name), category: props.category},
            url: "/ZotRides/api/get-image",
            success: (resultData) => {
                setImageurl(resultData.imageurl);
            }
        });
    }, [])

    function handleRedirect() {
        console.log("yes")
        props.handleCategorySelect(props.category);
    }

    return (
        <Col xs={4} className={"pt-3"}>
            <Card className={'text-center card'}>
                <LinkContainer to={'/cars/' + props.id}>
                    <Card.Img
                        className={'mx-auto'}
                        variant={'top'}
                        src={imageurl}
                    />
                </LinkContainer>
                <Card.Body className={'pt-1'}>
                    <LinkContainer to={'/cars/' + props.id}>
                        <Card.Title style={{fontSize: '30px'}} className={'mb-1'}>
                            {props.name}
                        </Card.Title>
                    </LinkContainer>
                    <p onClick={handleRedirect}>{props.category}</p>
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

function jankyCarMake(name) {
    var temp = name.split(" ")[0];
    if(temp == "Alfa")
        return "Alfa Romeo";
    else if(temp == "Aston")
        return "Aston Martin";
    else
        return temp;
}