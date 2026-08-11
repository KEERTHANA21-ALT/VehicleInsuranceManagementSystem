import { Link, useNavigate } from "react-router";

function AdminNavbar() {
    const navigate = useNavigate();

    const onLogout = () => {
        localStorage.clear();
        navigate("/");
    };

    return (
        <div>
            <nav className="navbar navbar-expand-lg bg-light sticky-top shadow">
                <div className="container">
                    <a className="navbar-brand fw-bold text-primary" href="#">EliteDrive</a>

                    <button className="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#adminNavbar">
                        <span className="navbar-toggler-icon"></span>
                    </button>

                    <div className="collapse navbar-collapse" id="adminNavbar">
                        <ul className="navbar-nav ms-auto">
                            <li className="nav-item"><Link to="/admin"  
                            className="nav-link active">Home</Link></li>
                            <li className="nav-item"><Link to="/admin/employees" className="nav-link">Employees</Link></li>
                            <li className="nav-item"><Link to ="/admin/customers" className="nav-link" >Customers</Link></li>
                            <li className="nav-item" ><Link to="/admin/insurance-plan" className="nav-link" >Insurance Plans</Link></li>
                            <li className="nav-item" ><Link to="/admin/proposals" className="nav-link">Proposals</Link></li>
                        </ul>

                        <button className="btn btn-primary ms-3" onClick={onLogout}>Logout</button>
                    </div>
                </div>
            </nav>
        </div>
    );
}

export default AdminNavbar;