import {useState} from 'react';
import {Nav, Navbar} from 'react-bootstrap';
import {LinkContainer} from 'react-router-bootstrap';
import SearchModal from "./Search/SearchModal";
import {Link} from "react-router-dom";

export default function MyNav(props) {
    const [modalShow, setModalShow] = useState(false);

    const handleClick = () => setModalShow(true);

    return (
        <div>
            <SearchModal show={modalShow}
                         onHide={() => setModalShow(false)}
                         setModalShow={setModalShow}
            />
            <Navbar variant="light" className={"myNav"}>
                <LinkContainer to="/">
                    <Navbar.Brand>
                        <img
                            src={"https://i.ibb.co/hKLvRdN/Screen-Shot-2021-04-16-at-4-24-19-PM.png"}
                            style={{width: "145px"}}
                        />
                    </Navbar.Brand>
                </LinkContainer>
                <Navbar.Toggle aria-controls="basic-navbar-nav"/>
                <Navbar.Collapse id="basic-navbar-nav">
                    <Nav className={"mr-auto"}>
                        <Nav.Link onClick={handleClick}>Search</Nav.Link>
                        <LinkContainer to={"/browse/type"}>
                            <Nav.Link>Browse by Vehicle Types</Nav.Link>
                        </LinkContainer>
                        <LinkContainer to={"/browse/name"}>
                            <Nav.Link>Browse by Vehicle Name</Nav.Link>
                        </LinkContainer>
                    </Nav>
                    <Nav className={"ml-auto"}>
                        {/*// TODO: Connecting search with variable flag*/}
                        <LinkContainer to="/profile">
                            <Nav.Link>Profile</Nav.Link>
                        </LinkContainer>
                        <LinkContainer to="/cart">
                            <Nav.Link>My Cart</Nav.Link>
                        </LinkContainer>
                        <LinkContainer to="/cart">
                            <Nav.Link>Sign out</Nav.Link>
                        </LinkContainer>
                    </Nav>
                </Navbar.Collapse>
            </Navbar>
        </div>
    )
}