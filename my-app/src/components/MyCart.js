import Header from "./Template/Header";
import CartItem from "./MyCart/CartItem";
import {Modal, Row, Col, Image} from "react-bootstrap";
import UpdateCartForm from './MyCart/UpdateCartForm';
import SearchForm from "./Search/SearchModal/SearchForm";
import {useEffect} from "react";
import jQuery from "jquery";
import HOST from "../Host";

export default function() {

    useEffect(() => {

        jQuery.ajax({
            dataType: "json",
            method: "GET",
            url: HOST + "api/shopping-cart",
            success: (resultData) => {
                // setCars(resultData);
                // setLoading(false);
                console.log(resultData);
            }
        });
    }, [])

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