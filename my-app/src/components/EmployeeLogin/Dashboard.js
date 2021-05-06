import {Col, Row} from "react-bootstrap";
import DBTable from "./Dashboard/Table";
import AddCarForm from "./Dashboard/AddCarForm";
import AddPickupForm from "./Dashboard/AddPickupForm";

export default function() {

    return (
        <div>
            <h1>Dashboard</h1>
            <h3 id={"tables"}>Tables</h3>
            <Row>
                <Col xs={4}>
                    <DBTable dbtitle={"DB Title"} attributes={{'attribute': 'string'}}/>
                </Col>
            </Row>
            <hr />
            <h3 id={"addcar"}>Add car</h3>
            <Row>
                <Col>
                    <AddCarForm />
                </Col>
            </Row>
            <hr />
            <h3 id={"addpickup"}>Add Pickup Location</h3>
            <Row>
                <Col>
                    <AddPickupForm />
                </Col>
            </Row>
        </div>
    )
}