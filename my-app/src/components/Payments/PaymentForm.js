import jQuery from "jquery";
import HOST from "../../Host";
import {Button, Form, Row, Col} from "react-bootstrap";
import {Formik} from "formik";
import React from "react";

export default function() {
    return (
        <Formik
            initialValues={{}}
            onSubmit={(async (values) => {
                console.log(values);
                // props.setLoading(true);
                // console.log(JSON.parse(JSON.stringify(values)));
                // jQuery.ajax({
                //     dataType: "json",
                //     method: "POST",
                //     data: values,
                //     url: HOST + "api/login",
                //     success: (resultData) => {
                //         //setCar(resultData[0]);
                //         //setLoading(false);
                //         console.log(JSON.stringify(resultData));
                //     }
                // });
            })}
        >
            {({
                  handleSubmit,
                  handleChange,
                  handleBlur,
                  values,
                  touched,
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
                            <select
                                name="month"
                                value={values.month}
                                onChange={handleChange}
                                onBlur={handleBlur}
                                style={{ display: 'block' }}
                            >
                                <option value="" label="Select a month" />
                                {
                                    generateNumArray(13).map((num) => (
                                        <option value={num} key={num}>
                                            {num}
                                        </option>
                                    ))
                                }
                            </select>
                            <select
                                name="day"
                                value={values.day}
                                onChange={handleChange}
                                onBlur={handleBlur}
                                style={{ display: 'block' }}
                            >
                                <option value="" label="Select a day" />
                                {
                                    generateNumArray(32).map((num) => (
                                        <option value={num} key={num}>
                                            {num}
                                        </option>
                                    ))
                                }
                            </select>
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