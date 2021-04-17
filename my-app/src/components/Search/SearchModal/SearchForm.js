import {Button, Form} from "react-bootstrap";
import {Formik} from "formik";
import {Redirect} from "react-router";
import {useState} from "react";

export default function(props) {
    const [complete, setComplete] = useState(false);
    if (complete) {
        props.setModalShow(false);
        return (<Redirect to={"/search"}/>);
    }
    return (
        <Formik
            // TODO: When all are empty return movie list
            initialValues={{}}
            onSubmit={(async (values) => {
                // props.setLoading(true);
                console.log(values);
                setComplete(true);
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
                        <Form.Label>Car Name</Form.Label>
                        <Form.Control
                            type={"string"}
                            name={"name"}
                            id={"name"}
                            onChange={handleChange}
                            onBlur={handleBlur}
                            value={values.name}
                            placeholder={"Enter name"}
                        />
                    </Form.Group>
                    <Form.Group>
                        <Form.Label>Car Year</Form.Label>
                        <Form.Control
                            type={"string"}
                            name={"year"}
                            id={"year"}
                            onChange={handleChange}
                            onBlur={handleBlur}
                            value={values.year}
                            placeholder={"Enter year"}
                        />
                    </Form.Group>
                    <Form.Group>
                        <Form.Label>Car Make</Form.Label>
                        <Form.Control
                            type={"string"}
                            name={"make"}
                            id={"make"}
                            onChange={handleChange}
                            onBlur={handleBlur}
                            value={values.make}
                            placeholder={"Enter make"}
                        />
                    </Form.Group>
                    <Form.Group>
                        <Form.Label>Car Location</Form.Label>
                        <Form.Control
                            type={"string"}
                            name={"location"}
                            id={"location"}
                            onChange={handleChange}
                            onBlur={handleBlur}
                            value={values.location}
                            placeholder={"Enter location"}
                        />
                    </Form.Group>
                    <Button variant={"primary"} type={"submit"}>Search</Button>
                </Form>
            )}
        </Formik>
    )
}

const defaultQuery = {
    name: "null",
    year: "null",
    make: "null",
    location: "null"
}