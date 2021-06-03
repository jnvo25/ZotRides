import {Modal, Button} from "react-bootstrap";
import {useEffect, useState} from "react";
import jQuery from "jquery";
import HOST from "../../Host";
import {Redirect} from "react-router";

export default function (props) {
    const [categories, setCategories] = useState();
    const [isLoading, setLoading] = useState(true);
    const [redirectURL, setRedirectURL] = useState("");
    const [redirect, setRedirect] = useState(false);

    useEffect(() => {
        jQuery.ajax({
            dataType: "json",
            method: "GET",
            url: "/ZotRides/api/categories",
            success: (resultData) => {
                console.log(resultData);
                setCategories(resultData.map((element) => {
                    return element.category
                }));
                setLoading(false);
            }
        });
    }, []);

    function handleClick(event) {
        props.setModalShow(false);
        setRedirectURL("/browse/na/na/na/na/" + event.target.id);
        setRedirect(true);
    }
    if(redirect)
        return (<Redirect to={redirectURL}/>);

    if(isLoading)
        return <div></div>
    return(
        <Modal
            {...props}
            size="lg"
            aria-labelledby="contained-modal-title-vcenter"
            centered
        >
            <Modal.Header closeButton>
                <Modal.Title id="contained-modal-title-vcenter">
                    Search for Car
                </Modal.Title>
            </Modal.Header>
            <Modal.Body>
                {
                    categories.map((category) => (
                        <Button id={category} onClick={handleClick} variant={"link"}>{category}</Button>
                    ))
                }
            </Modal.Body>
        </Modal>
    )
}