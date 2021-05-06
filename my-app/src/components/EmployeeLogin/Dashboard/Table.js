import {Table} from "react-bootstrap";

export default function(props) {
    return (
        <div>
            <h5>{props.dbtitle}</h5>
            <Table striped bordered hover>
                <thead>
                <tr>
                    <th>Attributes</th>
                    <th>Type</th>
                </tr>
                </thead>
                <tbody>
                {
                    Object.keys(props.attributes).map((element) => (
                        <tr>
                            <th>{element}</th>
                            <th>{props.attributes[element]}</th>
                        </tr>
                    ))
                }
                </tbody>
            </Table>
        </div>
    )
}