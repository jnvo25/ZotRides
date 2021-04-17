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

function App() {
    return (
        <Router basename={"/cs122b_spring21_team_16_war"}>
            <Navbar />
            <Switch>
                <Route path="/browse/:query" render={(props) => <Browse {...props} />} />
                <Route path="/browse/:query/:category" render={(props) => <Browse {...props} />} />
                <Route path="/login" render={() => <Login />} />
                <Route path="/" exact render={() => <Home />} />
                <Route path="/search" render={() => <SearchResults />} />
                <Route path="/cars/:carId" render={(props) => <SingleCar {...props} />} />
                <Route path="/locations/:locationId" render={(props) => <SingleLocation {...props} />} />
            </Switch>
        </Router>
    );
}

export default App;
