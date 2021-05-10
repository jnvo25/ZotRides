import {Button, Form, Modal} from "react-bootstrap";
import {useState} from "react";
import {Formik} from "formik";
import jQuery from "jquery";
import HOST from "../../../Host";

export default function(props) {
    const [success, setSuccess] = useState(false);
    const [message, setMessage] = useState("");

    const handleClose = () => setSuccess(false);

    if(success) {
        return (
            <Modal show={success} onHide={handleClose}>
                <Modal.Header closeButton>
                    <Modal.Title>Response Received!</Modal.Title>
                </Modal.Header>
                <Modal.Body>{message}</Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={handleClose}>
                        Close
                    </Button>
                </Modal.Footer>
            </Modal>
        )
    }

    return (
        <Formik
            // TODO: When all are empty return movie list
            initialValues={{}}
            onSubmit={(async (values) => {
                jQuery.ajax({
                    dataType: "json",
                    method: "POST",
                    data: values,
                    url: HOST + "api/add-car",
                    success: (resultData) => {
                        if(resultData.errorMessage != null) {
                            props.setError([resultData.errorMessage, ""]);
                        } else {
                            setMessage(resultData.message);
                            setSuccess(true);
                        }
                    },
                    error: (resultData) => {
                        console.log("ERROr")
                        console.log(resultData)
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
                        <Form.Label>Car address</Form.Label>
                        <Form.Control
                            type={"string"}
                            name={"address"}
                            id={"address"}
                            onChange={handleChange}
                            onBlur={handleBlur}
                            value={values.address}
                            placeholder={"Enter address"}
                        />
                    </Form.Group>
                    <Button variant={"primary"} type={"submit"}>Add</Button>
                </Form>
            )}
        </Formik>
    )
}