import {Button, Card} from "react-bootstrap";

export default function(props) {
    return (
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
                <p>Days: {daysInBetween(props.start, props.end)}</p>
                <p>Pickup Location: {props.location}</p>
                <p>Start Date: {props.start}</p>
                <p>End Date: {props.end}</p>
            </Card.Body>

        </Card>
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