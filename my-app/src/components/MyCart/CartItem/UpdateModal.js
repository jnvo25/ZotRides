import {Col, Modal, Row} from "react-bootstrap";
import UpdateCartForm from "../UpdateCartForm";

export default function() {
    return (
        <Modal
            show={false}
            size="lg"
            aria-labelledby="contained-modal-title-vcenter"
            centered
        >
            <Modal.Header closeButton>
                <Modal.Title id="contained-modal-title-vcenter">
                    Update Reservation
                </Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <Row>
                    <Col xs={6}
                         style={{
                             backgroundImage: "url('https://d1zgdcrdir5wgt.cloudfront.net/media/vehicle/images/-GWIN1aUTRO8HI2WwVEzIA.1440x700.jpg')",
                             backgroundSize: "cover",
                             backgroundPosition: "center"
                         }}>
                    </Col>
                    <Col>
                        <UpdateCartForm
                            locations={['a', 'b']}
                            locationids={['a','b']}
                        />
                    </Col>
                </Row>
            </Modal.Body>
        </Modal>
    )
}