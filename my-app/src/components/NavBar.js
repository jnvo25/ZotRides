import {useState} from 'react';
import {Nav, Navbar} from 'react-bootstrap';
import {LinkContainer} from 'react-router-bootstrap';
import SearchModal from "./Search/SearchModal";

export default function MyNav() {
    const [modalShow, setModalShow] = useState(false);

    return (
        <div>
            <SearchModal show={modalShow}
                         onHide={() => setModalShow(false)}
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
                    <Nav className={"ml-auto"}>
                        {/*// TODO: Connecting search with variable flag*/}
                        <LinkContainer to="/search" exact>
                            <Nav.Link>Search</Nav.Link>
                        </LinkContainer>
                        <LinkContainer to="/profile">
                            <Nav.Link>Profile</Nav.Link>
                        </LinkContainer>
                        <LinkContainer to="/cart">
                            <Nav.Link>My Cart</Nav.Link>
                        </LinkContainer>
                    </Nav>
                </Navbar.Collapse>
            </Navbar>
        </div>
    )
}