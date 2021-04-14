import logo from './logo.svg';
import './App.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import {BrowserRouter as Router, Switch, Route} from 'react-router-dom';

import Login from './components/Login';
import Home from './components/Home';
import SingleCar from './components/SingleCar';
import SingleLocation from './components/SingleLocation';

function App() {
  return (
    <Router basename={"/cs122b_spring21_team_16_war"}>
        <Switch>
            <Route path="/login" render={() => <Login />} />
            <Route path="/" exact render={() => <Home />} />
            <Route path="/cars/:carId" render={(props) => <SingleCar {...props} />} />
            <Route path="/locations/:locationId" render={(props) => <SingleLocation {...props} />} />
        </Switch>
    </Router>
  );
}

export default App;
