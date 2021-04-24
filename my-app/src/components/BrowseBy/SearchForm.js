import {Button, Form} from "react-bootstrap";
import {Formik} from "formik";
import {Redirect} from "react-router";
import {useState} from "react";
import jQuery from "jquery";

export default function() {
    const [complete, setComplete] = useState(false);
    const [query, setQuery] = useState({defaultQuery});
    if (complete) {
        return (<Redirect to={"/browse" + "/" + query.model + "/" + query.year + "/" + query.make + "/" + query.location}/>);
    }
    return (
        <Formik
            // TODO: When all are empty return movie list
            initialValues={{}}
            onSubmit={(async (values) => {
                // props.setLoading(true);
                if (values.model == null)
                    values.model = 'na';
                if (values.year == null)
                    values.year = 'na';
                if (values.make == null)
                    values.make = 'na';
                if (values.location == null)
                    values.location = 'na';
                setQuery(values);
                setComplete(true);
                window.location.reload(false);
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
                        <Form.Label>Car Model</Form.Label>
                        <Form.Control
                            type={"string"}
                            name={"model"}
                            id={"model"}
                            onChange={handleChange}
                            onBlur={handleBlur}
                            value={values.model}
                            placeholder={"Enter model name"}
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
    model: "na",
    year: "na",
    make: "na",
    location: "na"
}