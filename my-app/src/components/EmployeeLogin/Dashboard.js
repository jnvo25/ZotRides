import {Col, Row} from "react-bootstrap";
import DBTable from "./Dashboard/Table";
import AddCarForm from "./Dashboard/AddCarForm";
import AddPickupForm from "./Dashboard/AddPickupForm";
import {useEffect, useState} from "react";
import jQuery from "jquery";
import HOST from "../../Host";

export default function() {
    const [tables, setTables] = useState([]);
    const [isLoading, setLoading] = useState(true);
    useEffect(() => {
        jQuery.ajax({
            dataType: "json",
            method: "GET",
            url: HOST + "api/metadata",
            success: (resultData) => {
                setTables(Object.values(resultData));
                setLoading(false);
            }
            // setComplete(true);
        });
    }, [])

    if(isLoading) {
        return (<div>Loading...</div>)
    }
    return (
        <div>
            <h1>Dashboard</h1>
            <h3 id={"tables"}>Tables</h3>
            <Row>
                {
                    tables.map((element) => (
                        <Col xs={3} >
                            <DBTable
                                dbtitle={element['tableName']}
                                fields={element['fields']}
                                types={element['types']}
                            />
                        </Col>
                    ))
                }
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
            <div className={"end"}/>
        </div>
    )
}