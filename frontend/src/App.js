import logo from './logo.svg';
import './App.css';
import {Router} from "./components/Router/Router";
import {Header} from "./components/Header/Header";
import {useSelector} from "react-redux";
import {Modal} from "./components/Modal/Modal";

function App() {
  const modal = useSelector(store => store.object.modal);

  return (
    <div className="App">
        {modal.visible &&
            <Modal type={modal.type} data={modal.data}/>
        }
        <Header/>
        <Router/>
    </div>
  );
}

export default App;
