import DatePicker from "react-date-picker";
import {Form, Button, Col, Dropdown, ListGroup} from "react-bootstrap";
import React, {useState} from "react";
import jQuery from "jquery";
import {Formik} from "formik";
import {LinkContainer} from "react-router-bootstrap";

export default function(props) {
    return (
        <Formik
            initialValues={{}}
            onSubmit={(async (values) => {
                // props.setLoading(true);
                // console.log(JSON.parse(JSON.stringify(values)));
                console.log(values);

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
                            name="location"
                            value={values.location}
                            onChange={handleChange}
                            onBlur={handleBlur}
                            style={{ display: 'block' }}
                        >
                            <option value="" label="Select a location" />
                            {
                                props.locations.map((location, index) => (
                                    <option value={props.locationids[index]} key={index}>
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
const difficultyOptions = [
    { value: 'beginner', label: 'Beginner'},
    { value: 'intermediate', label: 'Intermediate'},
    { value: 'advanced', label: 'Advanced'},
];

const mealTimeOptions = [
    { value: 'breakfast', label: 'Breakfast'},
    { value: 'lunch', label: 'Lunch'},
    { value: 'dinner', label: 'Dinner'},
    { value: 'snacks', label: 'Snack'},
];

const dishTypeOptions = [
    { value: 'entrees', label: 'Entree'},
    { value: 'sides', label: 'Side'},
    { value: 'sauces', label: 'Sauce'},
    { value: 'appetizers', label: 'Appetizer'},
    { value: 'pastries', label: 'Pastry'},
]


const groupedOptions = [
    {
        label: 'Difficulty',
        options: difficultyOptions
    },
    {
        label: 'Meal Time',
        options: mealTimeOptions
    },
    {
        label: 'Dish Type',
        options: dishTypeOptions
    }
]