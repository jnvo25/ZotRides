import {Button, ButtonGroup, Col, Container, Dropdown, Row} from "react-bootstrap";
import CarCard from "../Home/CarCard";

export default function(props) {

    return(
        <Container fluid>
            <Row>
                <Col>
                    <ButtonGroup>
                        {
                            props.options.map((element, index) => (
                                <Button variant={"link"} id={element} onClick={props.handleOptionSelect} key={index}>{element}</Button>
                            ))
                        }
                    </ButtonGroup>
                </Col>
            </Row>
            <Row>
                {props.cars.map((car, index) => (
                    <CarCard
                        key={index}
                        id={car.car_id}
                        name={car.car_name}
                        category={car.car_category}
                        rating={car.car_rating}
                        votes={car.car_votes}
                        locations={car.location_address.split(';')}
                        locationId={car.location_ids.split(';')}
                        phone={car.location_phone.split(';')}
                    />
                ))}
            </Row>
        </Container>
    )
}