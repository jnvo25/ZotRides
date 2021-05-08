import {Table} from "react-bootstrap";

export default function(props) {
    console.log(props);
    if(props.dbtitle === undefined)
        return <div></div>
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
                    props.fields.map((element, index)=> (
                        <tr>
                            <th>{element}</th>
                            <th>{props.types[index]}</th>
                        </tr>
                    ))
                }
                </tbody>
            </Table>
        </div>
    )
}