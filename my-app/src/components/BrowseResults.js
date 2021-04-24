import {useEffect, useState} from "react";
import jQuery from "jquery";
import HOST from "../Host";
import CarCard from "./Home/CarCard";
import {Container, Row, Col, Button} from "react-bootstrap";

export default function(props) {
    const [cars, setCars] = useState({});
    const [isLoading, setLoading] = useState(true);
    const [message, setMessage] = useState("");

    useEffect(() => {
        const query = removeEmpty(props.match.params);
        if(query.retrace != null) {
            console.log("YO");
            jQuery.ajax({
                dataType: "json",
                method: "GET",
                url: HOST + "api/jump",
                success: (resultData) => {
                    console.log(resultData);
                    setCars(resultData.results);
                    setMessage(resultData.message);
                    setLoading(false);
                }
            });
        } else if(query.key != null && query.key.length === 1) {
            console.log("Letter: " + query.key);
            jQuery.ajax({
                dataType: "json",
                method: "POST",
                data: {model: query.key},
                url: HOST + "api/browse-car",
                success: (resultData) => {
                    setCars(resultData.results);
                    setMessage(resultData.message);
                    setLoading(false);
                }
            });
        } else if(query.key != null) {
            console.log("Category: " + query.key);
            jQuery.ajax({
                dataType: "json",
                method: "POST",
                data: {category: query.key},
                url: HOST + "api/browse-car",
                success: (resultData) => {
                    setMessage(resultData.message);
                    setCars(resultData.results);
                    setLoading(false);                }
            });
        } else {
            jQuery.ajax({
                dataType: "json",
                method: "POST",
                data: removeEmpty(props.match.params),
                url: HOST + "api/search-car",
                success: (resultData) => {
                    if (resultData.status === "fail")
                        props.setError("Login failed (Invalid username/password");
                    else {
                        setMessage(resultData.message);
                        setCars(resultData.results);
                        setLoading(false);
                    }
                }
            });
        }
    }, [])

    function handleNext() {
        setLoading(true);
        jQuery.ajax({
            dataType: "json",
            method: "POST",
            data: {pageStatus: 1},
            url: HOST + "api/paginate",
            success: (resultData) => {
                if (resultData.status === "fail")
                    props.setError("Login failed (Invalid username/password");
                else {
                    setMessage(resultData.message);
                    setCars(resultData.results);
                    setLoading(false);
                }
            }
        });
    }

    function handlePrevious() {
        setLoading(true);
        jQuery.ajax({
            dataType: "json",
            method: "POST",
            data: {pageStatus: -1},
            url: HOST + "api/paginate",
            success: (resultData) => {
                if (resultData.status === "fail")
                    props.setError("Login failed (Invalid username/password");
                else {
                    setMessage(resultData.message);
                    setCars(resultData.results);
                    setLoading(false);
                }
            }
        });
    }

    function setShown(event) {
        console.log(event.target.value);
        setLoading(true);
        jQuery.ajax({
            dataType: "json",
            method: "POST",
            data: {resultsPerPage: event.target.value},
            url: HOST + "api/paginate",
            success: (resultData) => {
                if (resultData.status === "fail")
                    props.setError("Login failed (Invalid username/password");
                else {
                    console.log(resultData);
                    setMessage(resultData.message);
                    setCars(resultData.results);
                    setLoading(false);
                }
            }
        });
    }

    if(isLoading)
        return(<div>LOADING</div>)
    return (
        <Container>
            <Row>
                <Col>
                    <Button onClick={handlePrevious}>Previous</Button>
                    <Button className={"ml-3"} onClick={handleNext}>Next</Button>
                </Col>
                <Col>
                    <p>{message}</p>
                </Col>
                <Col>
                    <select name="show" id="cars" onChange={setShown}>
                        <option>Set show</option>
                        <option value="10">10</option>
                        <option value="25">25</option>
                        <option value="50">50</option>
                        <option value="100">100</option>
                    </select>
                </Col>
            </Row>
            <Row>
                {cars.map((car, index) => (
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

function removeEmpty(object) {
    if(object.model === "na" || object.model == null)
        delete object.model;
    if(object.year === "na" || object.year == null)
        delete object.year;
    if(object.make === "na" || object.make == null)
        delete object.make;
    if(object.location === "na" || object.location == null)
        delete object.location;
    if(object.key === "na" || object.key == null)
        delete object.key;
    return object;
}