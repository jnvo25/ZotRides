import {useEffect, useState} from "react";
import jQuery from "jquery";
import HOST from "../Host";
import CarCard from "./Home/CarCard";
import {Container, Row, Col, Button} from "react-bootstrap";

export default function(props) {
    const [cars, setCars] = useState({});
    const [isLoading, setLoading] = useState(true);
    const [message, setMessage] = useState("");
    const [whoFirst, setFirst] = useState(0);
    const [ratingDescend, setRatingDescend] = useState(0);
    const [nameDescend, setNameDescend] = useState(0);
    const [redirect, setRedirect] = useState("");


    useEffect(() => {
        const query = removeEmpty(props.match.params);
        console.log(query);
        if(query.retrace != null) {
            jQuery.ajax({
                dataType: "json",
                method: "GET",
                url: HOST + "api/jump",
                success: (resultData) => {
                    console.log(resultData);
                    setFirst(resultData.sorting.ratingFirst);
                    setRatingDescend(resultData.sorting.ratingDescend);
                    setNameDescend(resultData.sorting.nameDescend);
                    setCars(resultData.results);
                    setMessage(resultData.message);
                    setLoading(false);
                }
            });
        } else if(query.key != null && query.key.length === 1) {
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
        } else if(query.fulltext != null) {
            jQuery.ajax({
                dataType: "json",
                method: "POST",
                data: {token: query.fulltext.replace("%20", " ")},
                url: HOST + "api/full-text-search",
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

    function handleRatingSort(event) {
        setRatingDescend(event.target.value);
        sendSortQuery({
            ratingFirst: whoFirst,
            ratingDescend: event.target.value,
            nameDescend: nameDescend
        });
    }

    function handleNameSort(event) {
        setNameDescend(event.target.value);
        sendSortQuery({
            ratingFirst: whoFirst,
            ratingDescend: ratingDescend,
            nameDescend: event.target.value
        });
    }

    function handleSortPref(event) {
        setFirst(event.target.value);
        console.log("ratingFirst: " + event.target.value);
        sendSortQuery({
            ratingFirst: event.target.value,
            ratingDescend: ratingDescend,
            nameDescend: nameDescend
        });
    }

    function sendSortQuery(data) {
        setLoading(true);
        console.log("Sending:");
        console.log(data)
        jQuery.ajax({
            dataType: "json",
            method: "POST",
            data: data,
            url: HOST + "api/sort",
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

    function handleCategorySelect(key) {
        setLoading(true);
        jQuery.ajax({
            dataType: "json",
            method: "POST",
            data: {category: key},
            url: HOST + "api/browse-car",
            success: (resultData) => {
                setMessage(resultData.message);
                setCars(resultData.results);
                setLoading(false);                }
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
                <Col>
                    <select name="sort" id="sort" value={whoFirst} onChange={handleSortPref}>
                        <option value="1">By Rating First</option>
                        <option value="0">By Title First</option>
                    </select>
                    <select name="sort" id="sort" value={nameDescend} onChange={handleNameSort}>
                        <option value="0">By Title Ascending</option>
                        <option value="1">By Title Descending</option>
                    </select>
                    <select name="sort" id="sort" value={ratingDescend} onChange={handleRatingSort}>
                        <option value="0">By Rating Ascending</option>
                        <option value="1">By Rating Descending</option>
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
                        handleCategorySelect={handleCategorySelect}
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
    if(object.retrace === "na" || object.retrace == null)
        delete object.retrace;
    if(object.fulltext === "na" || object.fulltext == null)
        delete object.fulltext;
    return object;
}