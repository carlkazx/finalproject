import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Sidebar from './components/Sidebar/Sidebar.jsx'; // Import your Sidebar component
import Header from './components/Header/Header.jsx'; // Import your Header component
import Dashboard from './pages/Dashboard/Dashboard.jsx';
import Tasks from "./pages/Tasks/Tasks.jsx";
import Details from "./pages/Tasks/Details.jsx"; // Import your Dashboard component

function App() {
    return (
        <Router>
            <div className="flex overflow-hidden ">
                <div className="basis-[15%] h-[100vh]">
                    <Sidebar/>
                </div>
                <div className="basis-[88%] border overflow-scroll h-[100vh]">
                    <Header/> {/* Header at the top */}
                    <div className="main-content border">
                        <Routes>
                            <Route path="/" element={<Dashboard/>}/>
                            <Route path="/tasks" element={<Tasks/>}/> {/* Add this line */}
                            <Route path="/task/:taskId" element={<Details />} />
                            {/*<Route path="/another-page" element={<AnotherPage />} />*/}
                            {/* Add more routes as needed */}
                        </Routes>
                    </div>
                </div>
            </div>
        </Router>
    );
}

export default App;
