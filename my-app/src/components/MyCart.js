import Header from "./Template/Header";
import CartItem from "./MyCart/CartItem";
import {Modal, Row, Col, Image} from "react-bootstrap";
import UpdateCartForm from './MyCart/UpdateCartForm';
import SearchForm from "./Search/SearchModal/SearchForm";

export default function() {
    return (
        <div>
            <Header title={"My Cart"}/>
            <CartItem
                name={'Car name'}
                price={'$45'}
                location={'Location'}
                start={'today'}
                end={'not today'}
            />
        </div>

    )
}