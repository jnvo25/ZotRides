import Header from "./Template/Header";
import CartItem from "./MyCart/CartItem";
import {Container, Modal, Row, Col, Image} from "react-bootstrap";
import UpdateCartForm from './MyCart/UpdateCartForm';
import SearchForm from "./Search/SearchModal/SearchForm";
import {useState, useEffect} from "react";
import jQuery from "jquery";
import HOST from "../Host";
import CarCard from "./Home/CarCard";

export default function() {
    const [isLoading, setLoading] = useState(true);
    const [cars, setCars] = useState([]);

    useEffect(() => {

        jQuery.ajax({
            dataType: "json",
            method: "GET",
            url: HOST + "api/shopping-cart",
            success: (resultData) => {
                setCars(resultData.previousItems);
                setLoading(false);
                console.log(resultData);
            }
        });
    }, [])

    if (isLoading) {
        return (<div>Loading...</div>)
    }
    return (
        <div>
        <Header title={"My Cart"}/>
        <Container>

            <Row>
                {cars.map((car, index) => (
                    <CartItem
                        key={index}
                        id={car.id}
                        name={car.name}
                        location={car.pickupLocation}
                        start={car.startDate}
                        end={car.endDate}
                    />
                ))}
            </Row>
        </Container>
        </div>

    )
}