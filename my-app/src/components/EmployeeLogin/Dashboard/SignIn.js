import { Formik } from 'formik';
import * as yup from 'yup';
import { Form, Button } from 'react-bootstrap';
import jQuery from 'jquery';
import HOST from '../../../Host';
import ReCaptcha from 'react-google-recaptcha';

export default function(props) {
    return (
        <Formik
            initialValues={{username:"c@email.com", password:"c2"}}
            onSubmit={(async (values) => {
                console.log(values);

                props.setLoading(true);
                props.setSuccess(true);
                // TODO: Connect to correct employee login
                // jQuery.ajax({
                //     dataType: "json",
                //     method: "POST",
                //     data: values,
                //     url: HOST + "api/login",
                //     success: (resultData) => {
                //         //setCar(resultData[0]);
                //         //setLoading(false);
                //         console.log(JSON.stringify(resultData));
                //         if(resultData.status === "fail")
                //             props.setError("Login failed (Invalid username/password");
                //         else
                //             props.setSuccess(true);
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
                        <Form.Label>Username</Form.Label>
                        <Form.Control
                            type={"string"}
                            name={"username"}
                            id={"username"}
                            onChange={handleChange}
                            onBlur={handleBlur}
                            value={values.username}
                            placeholder={"Enter username"}
                            isInvalid={touched.username && errors.username}
                        />
                        <Form.Control.Feedback type={"invalid"}>
                            {errors.username}
                        </Form.Control.Feedback>
                    </Form.Group>
                    <Form.Group>
                        <Form.Label>Password</Form.Label>
                        <Form.Control
                            type={"password"}
                            name={"password"}
                            id={"password"}
                            onChange={handleChange}
                            onBlur={handleBlur}
                            value={values.password}
                            placeholder={"Enter password"}
                            isInvalid={touched.password && errors.password}
                        />
                        <Form.Control.Feedback type={"invalid"}>
                            {errors.password}
                        </Form.Control.Feedback>
                    </Form.Group>
                    <Form.Group>
                        <ReCaptcha
                            sitekey={"6LdFer8aAAAAACdsGnif7x0qN-yvVYsYPlyMCIAW"}
                            onChange={(value) => values.recaptcha = value}
                        />
                    </Form.Group>
                    <Button variant={"primary"} type={"submit"}>Login</Button>
                    <Button variant={"link"} value={"register"} onClick={props.switchForm}>Create an Account</Button>
                </Form>
            )}
        </Formik>
    )
}