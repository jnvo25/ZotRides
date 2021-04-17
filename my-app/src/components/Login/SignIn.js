import { Formik } from 'formik';
import * as yup from 'yup';
import { Form, Button } from 'react-bootstrap';
import jQuery from 'jquery';

export default function(props) {
    return (
        <Formik
            initialValues={{ username: "", password: ""}}
            onSubmit={(async (values) => {
                // props.setLoading(true);
                // console.log(JSON.parse(JSON.stringify(values)));

                jQuery.ajax({
                    dataType: "json",
                    method: "POST",
                    data: values,
                    // TODO: REMOVE HTTP://LOCALHOST WHEN BUILDING
                    url: "http://localhost:8080/cs122b_spring21_team_16_war/api/login",
                    // url: "api/single-car?id=" + props.match.params.carId,
                    success: (resultData) => {
                        //setCar(resultData[0]);
                        //setLoading(false);
                        console.log(JSON.stringify(resultData));
                        if(resultData.status === "fail")
                            props.setError("Login failed (Invalid username/password");
                        else
                            props.setSuccess(true);
                        // TODO : link to home page if succeeded, otherwise tell them to try again
                    }
                });
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
                    <Button variant={"primary"} type={"submit"}>Login</Button>
                    <Button variant={"link"} value={"register"} onClick={props.switchForm}>Create an Account</Button>
                </Form>
            )}
        </Formik>
    )
}