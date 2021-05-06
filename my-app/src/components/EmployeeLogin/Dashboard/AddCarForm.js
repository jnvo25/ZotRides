import {Button, Form} from "react-bootstrap";
import {Formik} from "formik";

export default function() {
    return (
        <Formik
            // TODO: When all are empty return movie list
            initialValues={{}}
            onSubmit={(async (values) => {

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
                        <Form.Label>Car Category</Form.Label>
                        <Form.Control
                            type={"string"}
                            name={"category"}
                            id={"category"}
                            onChange={handleChange}
                            onBlur={handleBlur}
                            value={values.category}
                            placeholder={"Enter category"}
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
                    <Button variant={"primary"} type={"submit"}>Add</Button>
                </Form>
            )}
        </Formik>
    )
}