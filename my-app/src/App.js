import './App.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import {BrowserRouter as Router, Switch, Route} from 'react-router-dom';

import Login from './components/Login';
import Home from './components/Home';
import SingleCar from './components/SingleCar';
import SingleLocation from './components/SingleLocation';
import Navbar from './components/NavBar';
import Browse from "./components/Browse";
import SearchResults from "./components/SearchResults";
import MyCart from "./components/MyCart";
import Payments from "./components/Payments";
import BrowseResults from "./components/BrowseResults";
import BrowseBy from "./components/BrowseBy";
import EmployeeLogin from "./components/EmployeeLogin";

function App() {
    return (
        <Router basename={"/ZotRides"}>
            <Switch>
                <Route exact path={"/(login)"} component={LoginContainer} />
                <Route exact path={"/(_dashboard)"} component={EmployeeLoginContainer} />
                <Route component={DefaultContainer} />
            </Switch>
        </Router>
    );
}

const LoginContainer = () => (
    <div>
        <Route path="/login" render={() => <Login />} />
    </div>
)

const EmployeeLoginContainer = () => (
    <div>
        <Route path="/_dashboard" render={() => <EmployeeLogin />} />
    </div>
)

const DefaultContainer = () => (
    <div>
        <Navbar />
        {/*<Route path="/browse/:query" render={(props) => <Browse {...props} />} />*/}
        {/*<Route path="/browse/:query/:category" render={(props) => <Browse {...props} />} />*/}
        <Route path="/" exact render={() => <Home />} />
        <Route path="/search/:model?/:year?/:make?/:location?" render={(props) => <SearchResults {...props}/>} />
        <Route path="/browse/:model?/:year?/:make?/:location?/:key?/:retrace?" render={(props) => <BrowseResults {...props}/>} />
        <Route path="/mycart" render={() => <MyCart />} />
        <Route path="/payment" render={() => <Payments />} />
        <Route path="/cars/:carId" render={(props) => <SingleCar {...props} />} />
        <Route path="/locations/:locationId" render={(props) => <SingleLocation {...props} />} />
        <Route path="/browseby" render={() => <BrowseBy />} />
    </div>
)

export default App;
