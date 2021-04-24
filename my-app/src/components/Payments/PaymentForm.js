import jQuery from "jquery";
import HOST from "../../Host";
import {Button, Form, Row, Col} from "react-bootstrap";
import {Formik} from "formik";
import React from "react";

export default function() {
    return (
        <Formik
            initialValues={{firstname:"Terry", lastname:"Mitchell", cc:"107001", month:"06", day:"12", year:"2008"}}
            onSubmit={(async (values) => {
                console.log({
                    firstName: values.firstname,
                    lastName: values.lastname,
                    ccNumber: values.cc,
                    expDate: values.year + "/" + values.month + "/" + values.day
                });
                jQuery.ajax({
                    dataType: "json",
                    method: "POST",
                    data: {
                        firstName: values.firstname,
                        lastName: values.lastname,
                        ccNumber: values.cc,
                        expDate: values.year + "/" + values.month + "/" + values.day
                    },
                    url: HOST + "api/payment",
                    success: (resultData) => {
                        console.log(resultData);
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
              }) => (
                <Form noValidate onSubmit={handleSubmit}>
                    <Form.Group>
                        <Form.Label>First name</Form.Label>
                        <Form.Control
                            type={"string"}
                            name={"firstname"}
                            id={"firstname"}
                            onChange={handleChange}
                            onBlur={handleBlur}
                            value={values.firstname}
                            placeholder={"Enter First Name"}
                        />
                        <Form.Control.Feedback type={"invalid"}>
                            {errors.username}
                        </Form.Control.Feedback>
                    </Form.Group>
                    <Form.Group>
                        <Form.Label>Last Name</Form.Label>
                        <Form.Control
                            type={"string"}
                            name={"lastname"}
                            id={"lastname"}
                            onChange={handleChange}
                            onBlur={handleBlur}
                            value={values.lastname}
                            placeholder={"Enter Last Name"}
                        />
                    </Form.Group>
                    <Form.Group>
                        <Form.Label>Credit Card Number</Form.Label>
                        <Form.Control
                            type={"string"}
                            name={"cc"}
                            id={"cc"}
                            onChange={handleChange}
                            onBlur={handleBlur}
                            value={values.cc}
                            placeholder={"Enter Credit Card Number"}
                        />
                    </Form.Group>
                    <Form.Group>
                        <Form.Label>Expiration Date</Form.Label>
                        <Row>
                            <Col>
                            <Form.Control
                                type={"string"}
                                name={"year"}
                                id={"year"}
                                onChange={handleChange}
                                onBlur={handleBlur}
                                value={values.year}
                                placeholder={"Enter year"}
                            />
                            </Col>
                            <Col>
                            <Form.Control
                                type={"string"}
                                name={"month"}
                                id={"month"}
                                onChange={handleChange}
                                onBlur={handleBlur}
                                value={values.month}
                                placeholder={"Enter month"}
                            />
                            </Col>
                            <Col>
                            <Form.Control
                                type={"string"}
                                name={"day"}
                                id={"day"}
                                onChange={handleChange}
                                onBlur={handleBlur}
                                value={values.day}
                                placeholder={"Enter day"}
                            />
                            </Col>
                        </Row>

                    </Form.Group>
                    <Button variant={"primary"} type={"submit"}>Continue</Button>
                    {/*<Button variant={"link"} value={"register"} onClick={props.switchForm}>Create an Account</Button>*/}
                </Form>
            )}
        </Formik>
    )
}

function generateNumArray(total) {
    var result = [];
    var y = 0;
    for(var i=1; i<total; i++) {
        result[y++] = i;
    }
    return result;
}

function generateNumArrayWithStart(start, total) {
    var result = [];
    var y = 0;
    for(var i=start; i<total; i++) {
        result[y++] = i;
    }
    return result;
}