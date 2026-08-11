import { Link, useNavigate } from "react-router";

function EmployeeNavbar() {
    const navigate = useNavigate();
    const employeeRole = localStorage.getItem("employeeRole");

    const onLogout = () => {
        localStorage.clear();
        navigate("/");
    };

    return (
        <nav className="navbar navbar-expand-lg bg-white sticky-top shadow-sm">
            <div className="container">
                <Link to="/employee" className="navbar-brand fw-bold text-primary fs-3">
                    {/* <i className="bi bi-shield-check me-2"></i> */}
                    EliteDrive
                </Link>

                <button
                    className="navbar-toggler"
                    type="button"
                    data-bs-toggle="collapse"
                    data-bs-target="#employeeNav"
                >
                    <span className="navbar-toggler-icon"></span>
                </button>

                <div className="collapse navbar-collapse" id="employeeNav">
                    <ul className="navbar-nav ms-auto align-items-center">
                        <li className="nav-item">
                            <Link to="/employee" className="nav-link">
                                <i className="bi bi-house me-1"></i>
                                Home
                            </Link>
                        </li>

                        {employeeRole === "INSURANCE_MANAGER" && (
                            <>
                                <li className="nav-item">
                                    <Link to="/employee/proposals" className="nav-link">
                                        <i className="bi bi-file-earmark-text me-1"></i>
                                        Proposals
                                    </Link>
                                </li>

                                <li className="nav-item">
                                    <Link to="/employee/policy" className="nav-link">
                                        <i className="bi bi-file-earmark-check me-1"></i>
                                        Policies
                                    </Link>
                                </li>
                            </>
                        )}

                        {employeeRole === "CLAIM_MANAGER" && (
                            <li className="nav-item">
                                <Link to="/employee/claims" className="nav-link">
                                    <i className="bi bi-clipboard-check me-1"></i>
                                    Claims
                                </Link>
                            </li>
                        )}

                        {employeeRole === "SURVEYOR" && (
                            <li className="nav-item">
                                <Link to="/employee/surveys" className="nav-link">
                                    <i className="bi bi-car-front me-1"></i>
                                    Surveys
                                </Link>
                            </li>
                        )}
                    </ul>

                    <button className="btn btn-primary ms-3" onClick={onLogout}>
                        Logout
                    </button>
                </div>
            </div>
        </nav>
    );
}

export default EmployeeNavbar