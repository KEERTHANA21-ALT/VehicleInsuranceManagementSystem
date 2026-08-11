import { useState, useEffect } from "react";
import axios from "axios";
import AdminNavbar from "./AdminNavbar";

function EmployeeManagement() {
    const [employees, setEmployees] = useState([]);
    const [name, setName] = useState("");
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [role, setRole] = useState("");
    const [roleFilter, setRoleFilter] = useState("");
    const [editMode, setEditMode] = useState(false);
    const [selectedId, setSelectedId] = useState(null);

    const token = localStorage.getItem("token");

    const config = {
        headers: 
        { 
            Authorization: "Bearer " + token 
        }
    };

    const fetchEmployees = async () => {

        try {
            const response = await axios.get("http://localhost:8080/api/employee/get-all", config);
            setEmployees(response.data);
        } catch (error) {
            console.log(error);
        }
    };

    useEffect(() => {
        fetchEmployees();
    }, []);

    const saveEmployee = async () => {
        if (!name || !username || !password || !role) {
            alert("Fill all fields");
            return;
        }

        const data = {
            name,
            username,
            password,
            employeeRole: role,
            isActive: true
        };

        try {
            if (editMode) {
                await axios.put(`http://localhost:8080/api/employee/update/${selectedId}`, data, config);
                alert("Employee Updated Successfully");
            } else {
                await axios.post("http://localhost:8080/api/employee/add", data, config);
                alert("Employee Created Successfully");
            }
            clearForm();
            fetchEmployees();
        } catch (error) {
            console.log(error);
            alert(error.response?.data || "Operation Failed");
        }
    };

    const editEmployee = (emp) => {
        setEditMode(true);
        setSelectedId(emp.id);
        setName(emp.name);
        setUsername(emp.username);
        setPassword("");
        setRole(emp.employeeRole);
        window.scrollTo({ top: 0, behavior: "smooth" });
    };

    const toggleEmployeeStatus = async (emp) => {
        console.log("Sending ID:", emp.id);
        try {
            const response = await axios.put(`http://localhost:8080/api/employee/delete/${emp.id}`, {}, config);
            console.log("Backend Response:", response.data);
            fetchEmployees();
        } catch (error) {
            console.log(error.response);
            alert("Status update failed");
        }
    };

    const clearForm = () => {
        setName("");
        setUsername("");
        setPassword("");
        setRole("");
        setEditMode(false);
        setSelectedId(null);
    };

    const filterEmployee = async (employeeRole) => {
        setRoleFilter(employeeRole);

        if (employeeRole === "") {
            fetchEmployees();
            return;
        }

        try {
            const response = await axios.get(`http://localhost:8080/api/employee/get-byEmployeeRole/${employeeRole}`, config);
            setEmployees(response.data);
        } catch (error) {
            console.log(error);
        }
    };

    return (
        <div>
            <AdminNavbar />

            <div className="container py-4">
                <h2 className="mb-4">Employee Management</h2>

                {/* Create / Update Form */}
                <div className="card shadow mb-4">
                    <div className="card-body">
                        <h4 className="mb-3">{editMode ? "Update Employee" : "Create Employee"}</h4>

                        <div className="row">
                            <div className="col-md-6 mb-3">
                                <label className="form-label">Name</label>
                                <input className="form-control" value={name} onChange={(e) => setName(e.target.value)} />
                            </div>

                            <div className="col-md-6 mb-3">
                                <label className="form-label">Username</label>
                                <input className="form-control" value={username} onChange={(e) => setUsername(e.target.value)} />
                            </div>

                            <div className="col-md-6 mb-3">
                                <label className="form-label">Password</label>
                                <input type="password" className="form-control" value={password} onChange={(e) => setPassword(e.target.value)} />
                            </div>

                            <div className="col-md-6 mb-3">
                                <label className="form-label">Employee Role</label>
                                <select className="form-select" value={role} onChange={(e) => setRole(e.target.value)}>
                                    <option value="">Select Role</option>
                                    <option value="SURVEYOR">Surveyor</option>
                                    <option value="CLAIM_MANAGER">Claim Manager</option>
                                    <option value="INSURANCE_MANAGER">Insurance Manager</option>
                                </select>
                            </div>
                        </div>

                        <button className={editMode ? "btn btn-success me-2" : "btn btn-primary"} onClick={saveEmployee}>
                            {editMode ? "Update Employee" : "Create Employee"}
                        </button>

                        {editMode && (
                            <button className="btn btn-secondary" onClick={clearForm}>
                                Cancel
                            </button>
                        )}
                    </div>
                </div>

                {/* Filter */}
                <div className="card shadow mb-4">
                    <div className="card-body">
                        
                        <label className="form-label">Filter By Role</label>
                        <select className="form-select w-25" value={roleFilter} onChange={(e) => filterEmployee(e.target.value)}>
                            
                            <option value="">All Employees</option>
                            <option value="SURVEYOR">Surveyor</option>
                            <option value="CLAIM_MANAGER">Claim Manager</option>
                            <option value="INSURANCE_MANAGER">Insurance Manager</option>
                        </select>
                    </div>
                </div>

                {/* Employee Table */}
                <div className="card shadow">
                    <div className="card-body">
                        <h4 className="mb-3">Employee List</h4>

                        <table className="table table-bordered table-hover">
                            <thead className="table-primary">
                                <tr>
                                    <th>S.No</th>
                                    <th>Name</th>
                                    <th>Username</th>
                                    <th>Role</th>
                                    <th>Status</th>
                                    <th>Action</th>
                                </tr>
                            </thead>

                            <tbody>
                                {employees.map((emp, index) => (
                                    <tr key={emp.id}>
                                        <td>{index + 1}</td>
                                        <td>{emp.name}</td>
                                        <td>{emp.username}</td>
                                        <td>{emp.employeeRole}</td>
                                        <td>{emp.isActive ? "Active" : "Inactive"}</td>
                                        {/* <td>
                                            {emp.isActive ? (
                                                <span className="badge bg-success">Active</span>
                                            ) : (
                                                <span className="badge bg-danger">Inactive</span>
                                            )}
                                        </td> */}
                                        <td>
                                            <button className="btn btn-primary me-2 text-white btn-sm" onClick={() => editEmployee(emp)}>
                                                Update
                                            </button>
                                            <button
                                                className={emp.isActive ? "btn btn-danger me-1 btn-sm" : "btn btn-success me-1 btn-sm"}
                                                onClick={() => toggleEmployeeStatus(emp)}
                                            >
                                                {emp.isActive ? "Deactivate" : "Activate"}
                                            </button>
                                        </td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default EmployeeManagement;