import DatePicker from "react-date-picker";
import {Form, Button, Col, Dropdown, ListGroup} from "react-bootstrap";
import React, {useState} from "react";
import jQuery from "jquery";
import {Formik} from "formik";
import {LinkContainer} from "react-router-bootstrap";
import HOST from "../../Host";
import {Redirect} from "react-router";

export default function(props) {
    const [submitted, setSubmitted] = useState(false);
    if(submitted)
        return <Redirect to={"/mycart"} />
    return (
        <Formik
            initialValues={{
                name: props.name,
                cardID: props.cardID,
                unitPrice: props.unitPrice
            }}
            onSubmit={(async (values) => {
                jQuery.ajax({
                    dataType: "json",
                    method: "POST",
                    data: {
                        ...values,
                        endDate: dateToString(values.endDate),
                        startDate: dateToString(values.startDate)
                    },
                    url: HOST + "api/shopping-cart",
                    success: (resultData) => {
                        console.log(JSON.stringify(resultData));
                        setSubmitted(true);
                        // if(resultData.status === "fail")
                        //     props.setError("Login failed (Invalid username/password");
                        // else
                        //     props.setSuccess(true);
                    }
                });
            })}
        >
            {({
                  handleSubmit,
                  handleChange,
                  handleBlur,
                  values,
                  errors,
                  setFieldValue,
              }) => (
                <Form noValidate onSubmit={handleSubmit}>
                    <Form.Group>
                        <Form.Label>Start Date</Form.Label>
                        <br />
                        <DatePicker
                            name={"startDate"}
                            value={values.startDate}
                            onChange={(val) => setFieldValue('startDate', val)}
                        />
                        <Form.Control.Feedback type={"invalid"}>
                            {errors.username}
                        </Form.Control.Feedback>
                    </Form.Group>
                    <Form.Group>
                        <Form.Label>End Date</Form.Label>
                        <br />
                        <DatePicker
                            name={"endDate"}
                            value={values.endDate}
                            onChange={(val) => setFieldValue('endDate', val)}
                        />
                        <Form.Control.Feedback type={"invalid"}>
                            {errors.password}
                        </Form.Control.Feedback>
                    </Form.Group>
                    <Form.Group>
                        <Form.Label>Pickup Location</Form.Label>
                        <select
                            name="pickupLocation"
                            value={values.pickLocation}
                            onChange={handleChange}
                            onBlur={handleBlur}
                            style={{ display: 'block' }}
                        >
                            <option value="" label="Select a location" />
                            {
                                props.locations.map((location, index) => (
                                    <option value={location} key={index}>
                                        {location}
                                    </option>
                                ))
                            }
                        </select>

                    </Form.Group>

                    <Button variant={"primary"} type={"submit"}>Add to cart</Button>
                </Form>
            )}
        </Formik>
        // <div>
        //     <Form
        //     <h2>$317/day</h2>
        //     <hr />
        //     <h4>Trip start</h4>
        //     <DatePicker onChange={onStartChange} value={startDate}/>
        //     <h4>Trip end</h4>
        //     <DatePicker onChange={onEndChange} value={endDate}/>
        //     <h4>Pickup location</h4>
        //     <Dropdown>
        //         <Dropdown.Toggle variant="success" id="dropdown-basic">
        //             Pickup Location
        //         </Dropdown.Toggle>
        //
        //         <Dropdown.Menu>
        //             <Dropdown.Item href="#/action-1">Action</Dropdown.Item>
        //             <Dropdown.Item href="#/action-2">Another action</Dropdown.Item>
        //             <Dropdown.Item href="#/action-3">Something else</Dropdown.Item>
        //         </Dropdown.Menu>
        //     </Dropdown>
        //     <Button>Continue</Button>
        //     <hr />
        // </div>
    )
}

function dateToString(input) {
    return formatDate(input)
}

function formatDate(date) {
    var d = new Date(date),
        month = '' + (d.getMonth() + 1),
        day = '' + d.getDate(),
        year = d.getFullYear();

    if (month.length < 2)
        month = '0' + month;
    if (day.length < 2)
        day = '0' + day;

    return [year, month, day].join('/');
}