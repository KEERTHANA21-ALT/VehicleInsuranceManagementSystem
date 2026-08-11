import { Link, useNavigate } from "react-router"

function HolderNavbar() {
    const navigate = useNavigate()

    return (
        <nav className="navbar navbar-expand-lg bg-white sticky-top shadow">
            <div className="container">

                <a className="text-primary navbar-brand fw-bold" href="#">
                    EliteDrive
                </a>


                <button
                    className="navbar-toggler"
                    type="button"
                    data-bs-toggle="collapse"
                    data-bs-target="#policyMenu"
                >
                    <span className="navbar-toggler-icon"></span>
                </button>


                <div className="collapse navbar-collapse" id="policyMenu">
                    <ul className="navbar-nav ms-auto">
                        <li className="nav-item">
                            <button className="nav-link active" onClick={() => navigate("/holder")}>
                                Home
                            </button>
                        </li>

                        <li className="nav-item">
                            <button className="nav-link" onClick={() => navigate("/holder/vehicles")}>
                                Vehicles
                            </button>
                        </li>

                        <li className="nav-item">
                            <Link to="/holder/proposals" className="nav-link">
                                Proposals
                            </Link>
                        </li>

                        <li className="nav-item">
                            <Link to="/holder/policies" className="nav-link">
                                Policies
                            </Link>
                        </li>

                        <li className="nav-item">
                            <Link to = "/holder/claims" className="nav-link" >
                                Claims
                            </Link>
                        </li>




                        <li className="nav-item d-flex align-items-center">
                            <button
                                className="nav-link d-flex align-items-center"
                                onClick={() => navigate("/holder/profile")}
                                style={{
                                    color: "#6c757d",
                                    padding: "8px 10px",
                                    lineHeight: "1"
                                }}
                            >
                                <i
                                    className="bi bi-person-circle"
                                    style={{
                                        fontSize: "1.5rem"
                                    }}
                                ></i>
                            </button>
                        </li>






                    </ul>
                </div>
            </div>
        </nav>
    )
}

export default HolderNavbar