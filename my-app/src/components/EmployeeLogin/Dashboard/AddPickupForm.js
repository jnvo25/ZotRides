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
                        <Form.Label>Location*</Form.Label>
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
                        <Form.Label>Phone number</Form.Label>
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
                    <Button variant={"primary"} type={"submit"}>Add</Button>
                </Form>
            )}
        </Formik>
    )
}