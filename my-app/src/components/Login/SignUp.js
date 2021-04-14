import React from 'react';
import { Formik } from 'formik';
import * as yup from 'yup';

import { Form, Button } from "react-bootstrap";

export default function(props) {

    const validationSchema = yup.object().shape({
        username: yup.string()
            .min(3, "Must be at least 3 characters")
            .matches(/^[a-zA-Z0-9]*$/)
            .required("Username is required"),
        password: yup.string()
            .min(8, "Must be at least 8 characters")
            .matches(/[A-Z]/).typeError("Capital")
            .matches(/[a-z]/).typeError("Lowercase")
            .matches(/[0-9]/).typeError("Number")
            .required("Password is required"),
        confirmPassword: yup.string()
            .oneOf([yup.ref("password"), null], "Password does not match")
            .required('Please confirm password')
    })

    return (
        <Formik
            validationSchema={validationSchema}
            initialValues={{
                username: "",
                password: "",
                confirmPassword: ""
            }}
            onSubmit={ async (values, {resetForm}) => {
                // props.setLoading(true);
                const { username, password } = values;
                console.log(values);
            }}
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
                        <Form.Label>Username</Form.Label>
                        <Form.Control
                            type="string"
                            name="username"
                            id="username"
                            onChange={handleChange}
                            onBlur={handleBlur}
                            value={values.username}
                            placeholder="Enter username"
                            isValid={touched.username && !errors.username}
                            isInvalid={touched.username && errors.username}
                        />
                        <Form.Control.Feedback>Looks good!</Form.Control.Feedback>
                        <Form.Control.Feedback type="invalid">
                            Username cannot contain any special characters
                        </Form.Control.Feedback>
                    </Form.Group>
                    <Form.Group>
                        <Form.Label>Password</Form.Label>
                        <Form.Control
                            type="password"
                            name="password"
                            id="password"
                            onChange={handleChange}
                            onBlur={handleBlur}
                            value={values.password}
                            placeholder="Enter password"
                            isValid={touched.password && !errors.password}
                            isInvalid={touched.password && errors.password}
                        />
                        <Form.Text className="text-muted">
                            min 8 characters
                        </Form.Text>
                        <Form.Control.Feedback>Looks good!</Form.Control.Feedback>
                        <Form.Control.Feedback type="invalid">
                            {errors.password === 'password must match the following: "/[A-Z]/"' ?
                                "Password must contain an uppercase character (A-Z)" :
                                (errors.password === 'password must match the following: "/[a-z]/"') ?
                                    "Password must contain a lowercase character (a-z)" :
                                    (errors.password === 'password must match the following: "/[0-9]/"') ?
                                        "Password must contain a number (0-9)" : errors.password
                            }
                        </Form.Control.Feedback>
                    </Form.Group>
                    <Form.Group>
                        <Form.Label>Confirm Password</Form.Label>
                        <Form.Control
                            type="password"
                            name="confirmPassword"
                            id="confirmPassword"
                            onChange={handleChange}
                            onBlur={handleBlur}
                            value={values.confirmPassword}
                            placeholder="Confirm Password"
                            isValid={touched.confirmPassword && !errors.confirmPassword && !errors.password}
                            isInvalid={touched.confirmPassword && errors.confirmPassword && !errors.password}
                        />
                        <Form.Control.Feedback type="invalid">
                            {errors.password ? errors.password : errors.confirmPassword}
                        </Form.Control.Feedback>
                    </Form.Group>
                    <Button variant="primary" type="submit">
                        Submit
                    </Button>
                    <Button variant="link" value="login" onClick={props.switchForm}>
                        Login
                    </Button>
                </Form>
            )}
        </Formik>
    )
}
