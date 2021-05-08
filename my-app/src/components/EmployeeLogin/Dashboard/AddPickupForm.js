import {Button, Form} from "react-bootstrap";
import {Formik} from "formik";
import jQuery from "jquery";
import HOST from "../../../Host";

export default function() {
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
                        // props.setLoading(false);
                        console.log(JSON.stringify(resultData));
                        // if(resultData.status === "fail")
                        // props.setError("Add car failed");
                        // else
                        //     props.setSuccess(true);
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