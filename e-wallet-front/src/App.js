import './App.css';
import WalletComponent from './component/WalletComponent';
import LoginComponent from './component/LoginComponent';
import {
  BrowserRouter as Router,
  Switch,
  Route,
  Redirect
} from "react-router-dom";

function App() {
  return (
    <div className="App">
      <Switch>
        <Route path="/login" component={LoginComponent} />
        <Route path="/wallets" component={WalletComponent} />
        <Redirect exact from="/" to="/login" />
      </Switch>
    </div>
  );
}

export default App;
