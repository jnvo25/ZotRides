import {Button, Form, Modal} from "react-bootstrap";
import {Formik} from "formik";
import jQuery from "jquery";
import HOST from "../../../Host";
import {useState} from "react";

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
                    url: HOST + "api/add-loc",
                    success: (resultData) => {
                        if(resultData.errorMessage != null) {
                            props.setError(["", resultData.errorMessage]);
                        } else {
                            setMessage(resultData.message);
                            setSuccess(true);
                        }
                    },
                    error: (resultData) => {
                        console.log("ERROR")
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
                        <Form.Label>Address*</Form.Label>
                        <Form.Control
                            type={"string"}
                            name={"address"}
                            id={"address"}
                            onChange={handleChange}
                            onBlur={handleBlur}
                            value={values.address}
                            placeholder={"Enter address name"}
                        />
                    </Form.Group>
                    <Form.Group>
                        <Form.Label>Phone number</Form.Label>
                        <Form.Control
                            type={"string"}
                            name={"phoneNumber"}
                            id={"phoneNumber"}
                            onChange={handleChange}
                            onBlur={handleBlur}
                            value={values.phoneNumber}
                            placeholder={"Enter Phone Number"}
                        />
                    </Form.Group>
                    <Button variant={"primary"} type={"submit"}>Add</Button>
                </Form>
            )}
        </Formik>
    )
}