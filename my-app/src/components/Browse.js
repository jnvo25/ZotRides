import {Container, Row, Col, Dropdown} from "react-bootstrap";
import CarCard from "./Home/CarCard";
import {useEffect, useState} from "react";
import jQuery from "jquery";
import Header from "./Template/Header";
import Content from "./Browse/Content";

export default function (props) {
    const [isLoading, setLoading] = useState(true);
    const [cars, setCars] = useState([]);

    useEffect(() => {
        console.log(props.match.params);
        jQuery.ajax({
            dataType: "json",
            method: "GET",
            // TODO: REMOVE HTTP://LOCALHOST WHEN BUILDING
            url: "http://localhost:8080/cs122b_spring21_team_16_war/api/cars",
            // url: "api/cars",
            success: (resultData) => {
                setCars(resultData);
                setLoading(false);
                console.log(resultData);
            }
        });
    }, [])

    const handleOptionSelect = (event) => {
        console.log(event.target.id);
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
                    options={["Pickup", "Wagon", "Van", "Coupe", "Sedan", "Hatchback", "Convertible", "SUV"]}
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
