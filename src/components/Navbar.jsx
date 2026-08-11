import { useNavigate } from "react-router";

function Navbar() {
  const navigate = useNavigate();
  const token = localStorage.getItem("token");

  const onLogout = () => {
    localStorage.clear();
    navigate("/login");
  };

  return (
    <div>
      <nav className="navbar navbar-expand-lg bg-light sticky-top shadow">
        <div className="container">
          <a className="navbar-brand fw-bold text-primary" href="#">EliteDrive</a>

          <button className="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarTogglerDemo01">
            <span className="navbar-toggler-icon"></span>
          </button>

          <div className="collapse navbar-collapse" id="navbarTogglerDemo01">
            <ul className="navbar-nav ms-auto">
              <li className="nav-item"><a className="nav-link" href="#">Home</a></li>
              <li className="nav-item"><a className="nav-link" href="#plans">Insurance Plans</a></li>
              <li className="nav-item"><a className="nav-link" href="#services">Services</a></li>
              <li className="nav-item"><a className="nav-link" href="#why">Why Choose Us</a></li>
            </ul>

            {/* Login Content */}
            <div className="ms-3">
              {token === null ? (
                <button className="btn btn-primary" onClick={() => navigate("/login")}>Login</button>
              ) : (
                <button 
                className="btn btn-primary"
                onClick={onLogout}
              >
                Logout
              </button>
              )}
            </div>
          </div>
        </div>
      </nav>
    </div>
  );
}

export default Navbar;