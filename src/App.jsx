import {BrowserRouter as Router, Route, Routes, Navigate, Outlet} from 'react-router-dom';
import Sidebar from './components/Sidebar/Sidebar.jsx';
import Header from './components/Header/Header.jsx';
import Dashboard from './pages/Dashboard/Dashboard.jsx';
import Tasks from "./pages/Tasks/Tasks.jsx";
import Details from "./pages/Tasks/Details.jsx";
import OvertimeRequests from "./pages/Overtime/OvertimeRequests.jsx";
import RequestsForm from "./pages/Overtime/RequestsForm.jsx";
import Login from "./pages/Login/Login.jsx";
import Signup from "./pages/Signup/Signup.jsx";

function App() {
    return (
        <Router>
            <Routes>
                {/* Redirect to /login by default */}
                <Route path="/" element={<Navigate replace to="/login" />} />

                {/* Public Routes */}
                <Route path="/login" element={<Login />} />
                <Route path="/signup" element={<Signup />} />

                {/* Layout with Sidebar and Header for authenticated routes */}
                <Route path="/" element={<AuthenticatedLayout />}>
                    <Route path="dashboard" element={<Dashboard />} />
                    <Route path="tasks" element={<Tasks />} />
                    <Route path="task/:taskId" element={<Details />} />
                    <Route path="overtime-requests" element={<OvertimeRequests />} />
                    <Route path="request-form" element={<RequestsForm />} />
                    {/* Add more authenticated routes as needed */}
                </Route>
            </Routes>
        </Router>
    );
}

function AuthenticatedLayout() {
    return (
        <div className="flex overflow-hidden">
            <div className="basis-[15%] h-[100vh]">
                <Sidebar />
            </div>
            <div className="basis-[85%] border overflow-scroll h-[100vh]">
                <Header />
                <div className="main-content border">
                    <Outlet /> {/* Renders the nested routes */}
                </div>
            </div>
        </div>
    );
}

export default App;
