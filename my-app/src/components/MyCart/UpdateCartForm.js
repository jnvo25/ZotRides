import DatePicker from "react-date-picker";
import {Form, Button, Col, Dropdown, ListGroup} from "react-bootstrap";
import React, {useState} from "react";
import jQuery from "jquery";
import {Formik} from "formik";
import {LinkContainer} from "react-router-bootstrap";
import HOST from "../../Host";

export default function(props) {
    return (
        <Formik
            initialValues={{}}
            onSubmit={(async (values) => {
                jQuery.ajax({
                    dataType: "json",
                    method: "POST",
                    data: {
                        endDate: dateToString(values.endDate),
                        startDate: dateToString(values.startDate),
                        itemID: props.id
                    },
                    url: HOST + "api/modify-cart-item",
                    success: (resultData) => {
                        console.log(resultData)
                        window.location.reload(false);
                    }
                });
            })}
        >
            {({
                  handleSubmit,
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
                    <Button variant={"primary"} type={"submit"}>Add to cart</Button>
                </Form>
            )}
        </Formik>
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