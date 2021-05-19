import {useState} from 'react';
import {Button, Col, Row, Jumbotron, Container} from 'react-bootstrap';
import {Typeahead} from "react-bootstrap-typeahead";
import './stylesheets/Home.css';
import jQuery from "jquery";
import HOST from "../Host";
import {Redirect} from "react-router";

export default function Home() {
    const [singleSelections, setSingleSelections] = useState([]);
    const [text, setText] = useState();
    const [options, setOptions] = useState([]);
    const [success, setSuccess] = useState(false);
    const [redirectId, setRedirectId] = useState();
    const [redirect, setRedirect] = useState(false);

    function onChange(input) {
        setText(input);
        if(input.length >= 3) {
            console.log("Autocomplete initiated");
            jQuery.ajax({
                dataType: "json",
                method: "POST",
                data: {token: input},
                url: HOST + "api/autocomplete",
                success: (resultData) => {
                    setOptions(resultData.results);
                }
            });
        } else {
            setOptions([]);
        }
    }

    function onSelect(input) {
        console.log(input);
        setSingleSelections(input);
        options.map(element => {
            if(input[0] === element["car_name"])
                setRedirectId(element["car_id"]);
        })
        setSuccess(true);
    }

    function onKeyDown(e) {
        if(e.key === "Enter") {
            setRedirect(true);
        }
    }
    if(redirect)
        return (<Redirect to={"/browse/na/na/na/na/na/na/" + text}/>);
    if(success)
        return (<Redirect to={"/cars/" + redirectId}  />)
    return (
        <div>
            <Jumbotron className={"hero-section"}>
                <Container>
                    <h1>Welcome to ZotRides</h1>
                    <p className={"subtitle"}>Book unforgettable cars from locations around the world</p>
                    <Row>
                        <Col>
                            <Typeahead
                                onInputChange={onChange}
                                delay={300}
                                onKeyDown={onKeyDown}
                                id="basic-typeahead-single"
                                labelKey="name"
                                onChange={onSelect}
                                options={options.map((element => {return element["car_name"]}))}
                                placeholder="Enter model name..."
                                selected={singleSelections}

                            />
                        </Col>
                        <Col xs={1}>
                            <Button onClick={()=>{setRedirect(true)}}>Search</Button>
                        </Col>
                    </Row>
                </Container>
            </Jumbotron>
        </div>
    );
}