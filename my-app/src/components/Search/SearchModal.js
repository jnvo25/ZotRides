import {Modal} from "react-bootstrap";
import SearchForm from "./SearchModal/SearchForm";
import {useEffect} from "react";
import jQuery from "jquery";
import HOST from "../../Host";

export default function (props) {

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
                <SearchForm setModalShow={props.setModalShow}/>
            </Modal.Body>
        </Modal>
    )
}