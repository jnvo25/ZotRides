import {Button, Card} from "react-bootstrap";
import {useState} from "react";
import UpdateModal from "./CartItem/UpdateModal";
import jQuery from "jquery";
import HOST from "../../Host";

export default function(props) {

    const [showUpdate, setUpdate] = useState(false);

    function handleUpdate() {
        setUpdate(!showUpdate);
    }

    function removeItem() {
        jQuery.ajax({
            dataType: "json",
            method: "POST",
            data: {
                itemID: props.id
            },
            url: HOST + "api/delete-cart-item",
            success: (resultData) => {
                console.log(resultData);
                window.location.reload(false);
            }
        });
    }

    return (
        <div>
            <UpdateModal showUpdate={showUpdate} setUpdate={setUpdate} carID={props.id} />
            <Card className={'text-center'}>
                <Card.Img
                    className={'mx-auto'}
                    variant={'top'}
                    src={'https://d1zgdcrdir5wgt.cloudfront.net/media/vehicle/images/-GWIN1aUTRO8HI2WwVEzIA.1440x700.jpg'}
                />
                <Card.Body className={'pt-1'}>
                    <Card.Title style={{fontSize: '30px'}} className={'mb-1'}>
                        {props.name}
                    </Card.Title>
                    <p>Price per Day: ${props.price}</p>
                    <p>Total Price: ${props.price*daysInBetween(props.start, props.end)}</p>
                    <p>Days: {daysInBetween(props.start, props.end)}</p>
                    <p>Pickup Location: {props.location}</p>
                    <p>Start Date: {props.start}</p>
                    <p>End Date: {props.end}</p>
                    <Button onClick={handleUpdate}>Update</Button>
                    <Button className={"ml-4"} onClick={removeItem}>Remove</Button>
                </Card.Body>

            </Card>
        </div>
    )
}

function daysInBetween(start, end) {
    const oneDay = 24 * 60 * 60 * 1000; // hours*minutes*seconds*milliseconds
    const fDate = start.split("/");
    const sDate = end.split("/");
    const firstDate = new Date(fDate[0], fDate[1], fDate[2]);
    const secondDate = new Date(sDate[0], sDate[1], sDate[2]);
    return Math.round(Math.abs((firstDate - secondDate) / oneDay));
}