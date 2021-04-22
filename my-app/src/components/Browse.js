import {Container, Row, Col, Dropdown} from "react-bootstrap";
import CarCard from "./Home/CarCard";
import {useEffect, useState} from "react";
import jQuery from "jquery";
import Header from "./Template/Header";
import Content from "./Browse/Content";
import HOST from "../Host";

export default function (props) {
    const [isLoading, setLoading] = useState(true);
    const [cars, setCars] = useState([]);
    const [categories, setCategories] = useState([]);

    useEffect(() => {
        let data;
        if(props.match.params.query === "type") {
            data = {category: 'pickup'};
        } else {
            data = {model: 'a'};
        }
        jQuery.ajax({
            dataType: "json",
            method: "POST",
            data: data,
            url: HOST + "api/browse-car",
            success: (resultData) => {
                setCars(resultData);
                setLoading(false);
            }
        });

        // get categories for browsing
        jQuery.ajax({
            dataType: "json",
            method: "GET",
            url: HOST + "api/categories",
            success: (resultData) => {
                setCategories(resultData.map(type => type.category));
                // console.log(categories);
            }
        });
    }, [])

    const handleOptionSelect = (event) => {
        let data;

        if(props.match.params.query === "type") {
            data = {category: event.target.id.toString().toLowerCase()};
        } else {
            data = {model: event.target.id.toString()};
        }
        console.log(data)
        jQuery.ajax({
            dataType: "json",
            method: "POST",
            data: data,
            url: HOST + "api/browse-car",
            success: (resultData) => {
                setCars(resultData);
                setLoading(false);
                console.log(resultData);
            }
        });
    }

    if (isLoading) {
        return (<div>Loading...</div>)
    }

    return (
        <div>
            <Header title={"Browse by Vehicle " + props.match.params.query} />
            {
                props.match.params.query === "type" &&
                <Content
                    handleOptionSelect={handleOptionSelect}
                    cars={cars}
                    // TODO : double check get categories via a servlet instead of hardcode
                    options={categories}
                />
            }
            {
                props.match.params.query === "name" &&
                <Content
                    handleOptionSelect={handleOptionSelect}
                    cars={cars}
                    options={["A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"]}
                />
            }

        </div>
    );
}
